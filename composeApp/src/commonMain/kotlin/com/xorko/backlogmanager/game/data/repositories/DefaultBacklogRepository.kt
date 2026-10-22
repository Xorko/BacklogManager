package com.xorko.backlogmanager.game.data.repositories

import arrow.core.Either
import com.xorko.backlogmanager.core.domain.DataError
import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.game.data.dto.shared.SharedGameDto
import com.xorko.backlogmanager.game.data.dto.shared.mappers.toSharedGameDto
import com.xorko.backlogmanager.game.data.local.LocalBacklogDataSource
import com.xorko.backlogmanager.game.data.local.entities.SyncStatus
import com.xorko.backlogmanager.game.data.local.entities.toBackloggedGame
import com.xorko.backlogmanager.game.data.local.entities.toDto
import com.xorko.backlogmanager.game.data.local.entities.toEntity
import com.xorko.backlogmanager.game.data.network.RemoteBacklogDataSource
import com.xorko.backlogmanager.game.domain.BacklogRepository
import com.xorko.backlogmanager.game.domain.GameRepository
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.domain.models.Game
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlin.time.Clock

class DefaultBacklogRepository(
    private val localDataSource: LocalBacklogDataSource,
    private val remoteDataSource: RemoteBacklogDataSource,
    private val gameRepository: GameRepository,
    private val auth: Auth,
    private val json: Json,
) : BacklogRepository {
    private val currentUserId: String?
        get() = auth.currentUserOrNull()?.id

    override fun getBackloggedGames(): Flow<List<BackloggedGame>> =
        localDataSource.getBackloggedGames().map { entities ->
            entities.mapNotNull { it.toBackloggedGame() }
        }

    override fun getBackloggedGameById(gameId: Long): Flow<BackloggedGame?> =
        localDataSource.getBackloggedGameById(gameId).map { entity ->
            entity?.toBackloggedGame()
        }

    override fun isGameInBacklog(gameId: Long): Flow<Boolean> = localDataSource.isGameInBacklog(gameId)

    override suspend fun addToBacklog(game: Game): Either<DataError, Unit> =
        try {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val userId = currentUserId
            val backloggedGame =
                BackloggedGame.ToPlay(
                    id = game.id.toString(),
                    game = game,
                    backloggedDate = now,
                )
            val entity =
                backloggedGame.toEntity(
                    syncStatus = SyncStatus.PENDING_INSERT,
                    userId = userId,
                )
            localDataSource.upsertBackloggedGame(entity)
            Logger.d("Added game ${game.id} to backlog locally", tag = this::class.simpleName)

            if (userId != null) {
                remoteDataSource.upsertBackloggedGame(entity.toDto()).fold(
                    ifLeft = { error ->
                        Logger.w("Remote insert postponed: $error", tag = this::class.simpleName)
                    },
                    ifRight = {
                        localDataSource.upsertBackloggedGame(entity.copy(syncStatus = SyncStatus.SYNCED.name))
                        Logger.d("Remote insert succeeded for game ${game.id}", tag = this::class.simpleName)
                    },
                )
            }

            Either.Right(Unit)
        } catch (e: Exception) {
            Logger.e("Error adding game to backlog: ${e.message}", tag = this::class.simpleName)
            Either.Left(DataError.Local.UNKNOWN)
        }

    override suspend fun removeFromBacklog(gameId: Long): Either<DataError, Unit> =
        try {
            val userId = currentUserId
            localDataSource.deleteBackloggedGame(gameId)
            Logger.d("Removed game $gameId from backlog locally", tag = this::class.simpleName)

            if (userId != null) {
                remoteDataSource.deleteBackloggedGame(gameId, userId).fold(
                    ifLeft = { error ->
                        Logger.w("Remote delete postponed: $error", tag = this::class.simpleName)
                    },
                    ifRight = {
                        Logger.d("Remote delete succeeded for game $gameId", tag = this::class.simpleName)
                    },
                )
            }

            Either.Right(Unit)
        } catch (e: Exception) {
            Logger.e("Error removing game from backlog: ${e.message}", tag = this::class.simpleName)
            Either.Left(DataError.Local.UNKNOWN)
        }

    override suspend fun updateBackloggedGameStatus(backloggedGame: BackloggedGame): Either<DataError, Unit> =
        try {
            val userId = currentUserId
            val entity =
                backloggedGame.toEntity(
                    syncStatus = SyncStatus.PENDING_UPDATE,
                    userId = userId,
                )
            localDataSource.upsertBackloggedGame(entity)
            Logger.d("Updated status for game ${backloggedGame.game.id} in backlog locally", tag = this::class.simpleName)

            if (userId != null) {
                remoteDataSource.upsertBackloggedGame(entity.toDto()).fold(
                    ifLeft = { error ->
                        Logger.w("Remote update postponed: $error", tag = this::class.simpleName)
                    },
                    ifRight = {
                        localDataSource.upsertBackloggedGame(entity.copy(syncStatus = SyncStatus.SYNCED.name))
                        Logger.d("Remote update succeeded for game ${backloggedGame.game.id}", tag = this::class.simpleName)
                    },
                )
            }

            Either.Right(Unit)
        } catch (e: Exception) {
            Logger.e("Error updating backlogged game status: ${e.message}", tag = this::class.simpleName)
            Either.Left(DataError.Local.UNKNOWN)
        }

    override suspend fun sync(): Either<DataError, Unit> {
        val userId =
            currentUserId ?: run {
                Logger.d("Skipping sync: No user signed in", tag = this::class.simpleName)
                return Either.Right(Unit)
            }

        return try {
            val unsynced = localDataSource.getUnsyncedGames()
            for (entity in unsynced) {
                when (entity.syncStatus) {
                    SyncStatus.PENDING_INSERT.name, SyncStatus.PENDING_UPDATE.name -> {
                        remoteDataSource.upsertBackloggedGame(entity.copy(userId = userId).toDto()).fold(
                            ifLeft = { error ->
                                Logger.w("Failed to push unsynced item ${entity.gameId}: $error", tag = this::class.simpleName)
                            },
                            ifRight = {
                                localDataSource.upsertBackloggedGame(entity.copy(syncStatus = SyncStatus.SYNCED.name, userId = userId))
                            },
                        )
                    }

                    SyncStatus.PENDING_DELETE.name -> {
                        remoteDataSource.deleteBackloggedGame(entity.gameId, userId).fold(
                            ifLeft = { error ->
                                Logger.w("Failed to push deletion ${entity.gameId}: $error", tag = this::class.simpleName)
                            },
                            ifRight = {
                                localDataSource.deleteBackloggedGame(entity.gameId)
                            },
                        )
                    }
                }
            }

            remoteDataSource.getBackloggedGames(userId).map { remoteDtos ->
                remoteDtos.forEach { dto ->
                    val localEntity = localDataSource.getBackloggedGameById(dto.gameId).single()
                    if (localEntity != null) {
                        val updatedEntity =
                            dto.toEntity(
                                gameJson = localEntity.gameJson,
                                syncStatus = SyncStatus.SYNCED,
                            )
                        localDataSource.upsertBackloggedGame(updatedEntity)
                    } else {
                        gameRepository.getGameById(dto.gameId).fold(
                            ifLeft = { error ->
                                Logger.w("Failed to fetch game details for remote game ${dto.gameId}: $error", tag = this::class.simpleName)
                            },
                            ifRight = { game ->
                                if (game != null) {
                                    val gameDto = game.toSharedGameDto()
                                    val gameJsonString = json.encodeToString(SharedGameDto.serializer(), gameDto)
                                    val newEntity =
                                        dto.toEntity(
                                            gameJson = gameJsonString,
                                            syncStatus = SyncStatus.SYNCED,
                                        )
                                    localDataSource.upsertBackloggedGame(newEntity)
                                }
                            },
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Logger.e("Sync failed: ${e.message}", tag = this::class.simpleName)
            Either.Left(DataError.Local.UNKNOWN)
        }
    }
}

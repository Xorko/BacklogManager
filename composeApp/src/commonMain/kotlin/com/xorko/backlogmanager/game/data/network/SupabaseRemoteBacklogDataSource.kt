package com.xorko.backlogmanager.game.data.network

import arrow.core.Either
import com.xorko.backlogmanager.core.domain.DataError
import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.game.data.network.dto.BackloggedGameDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

class SupabaseRemoteBacklogDataSource(
    private val postgrest: Postgrest,
) : RemoteBacklogDataSource {
    private companion object {
        const val TABLE_NAME = "backlogged_games"
    }

    override suspend fun getBackloggedGames(userId: String): Either<DataError.Remote, List<BackloggedGameDto>> =
        try {
            val result =
                postgrest[TABLE_NAME]
                    .select(columns = Columns.ALL) {
                        filter {
                            eq("user_id", userId)
                        }
                    }.decodeList<BackloggedGameDto>()
            Either.Right(result)
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            Logger.e("Error fetching remote backlogged games: ${e.message}")
            Either.Left(DataError.Remote.UNKNOWN)
        }

    override suspend fun upsertBackloggedGame(dto: BackloggedGameDto): Either<DataError.Remote, Unit> =
        try {
            postgrest[TABLE_NAME].upsert(dto)
            Either.Right(Unit)
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            Logger.e("Error upserting remote backlogged game: ${e.message}")
            Either.Left(DataError.Remote.UNKNOWN)
        }

    override suspend fun deleteBackloggedGame(
        gameId: Long,
        userId: String,
    ): Either<DataError.Remote, Unit> =
        try {
            postgrest[TABLE_NAME].delete {
                filter {
                    eq("game_id", gameId)
                    eq("user_id", userId)
                }
            }
            Either.Right(Unit)
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            Logger.e("Error deleting remote backlogged game: ${e.message}")
            Either.Left(DataError.Remote.UNKNOWN)
        }
}

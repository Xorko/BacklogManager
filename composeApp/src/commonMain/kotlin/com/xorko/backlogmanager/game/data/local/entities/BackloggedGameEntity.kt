package com.xorko.backlogmanager.game.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xorko.backlogmanager.game.data.dto.shared.SharedGameDto
import com.xorko.backlogmanager.game.data.dto.shared.mappers.toGame
import com.xorko.backlogmanager.game.data.dto.shared.mappers.toSharedGameDto
import com.xorko.backlogmanager.game.data.network.dto.BackloggedGameDto
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import kotlin.time.Clock

enum class SyncStatus {
    SYNCED,
    PENDING_INSERT,
    PENDING_UPDATE,
    PENDING_DELETE,
}

const val STATUS_TO_PLAY = "TO_PLAY"
const val STATUS_NOW_PLAYING = "NOW_PLAYING"
const val STATUS_FINISHED = "FINISHED"

@Entity(tableName = "backlogged_games")
data class BackloggedGameEntity(
    @PrimaryKey val gameId: Long,
    val status: String,
    val backloggedDate: String,
    val startedDate: String? = null,
    val finishedDate: String? = null,
    val gameJson: String,
    val syncStatus: String = SyncStatus.PENDING_INSERT.name,
    val userId: String? = null,
    val lastModifiedAt: Long = 0L,
)

fun BackloggedGame.toEntity(
    syncStatus: SyncStatus = SyncStatus.PENDING_INSERT,
    userId: String? = null,
    json: Json = Json { ignoreUnknownKeys = true },
): BackloggedGameEntity {
    val gameDto = game.toSharedGameDto()
    val gameJsonString = json.encodeToString(SharedGameDto.serializer(), gameDto)
    val nowMillis = Clock.System.now().toEpochMilliseconds()

    return when (this) {
        is BackloggedGame.ToPlay -> {
            BackloggedGameEntity(
                gameId = game.id,
                status = STATUS_TO_PLAY,
                backloggedDate = backloggedDate.toString(),
                startedDate = null,
                finishedDate = null,
                gameJson = gameJsonString,
                syncStatus = syncStatus.name,
                userId = userId,
                lastModifiedAt = nowMillis,
            )
        }

        is BackloggedGame.Playing -> {
            BackloggedGameEntity(
                gameId = game.id,
                status = STATUS_NOW_PLAYING,
                backloggedDate = backloggedDate.toString(),
                startedDate = startedDate.toString(),
                finishedDate = null,
                gameJson = gameJsonString,
                syncStatus = syncStatus.name,
                userId = userId,
                lastModifiedAt = nowMillis,
            )
        }

        is BackloggedGame.Finished -> {
            BackloggedGameEntity(
                gameId = game.id,
                status = STATUS_FINISHED,
                backloggedDate = backloggedDate.toString(),
                startedDate = startedDate.toString(),
                finishedDate = finishedDate.toString(),
                gameJson = gameJsonString,
                syncStatus = syncStatus.name,
                userId = userId,
                lastModifiedAt = nowMillis,
            )
        }
    }
}

fun BackloggedGameEntity.toBackloggedGame(json: Json = Json { ignoreUnknownKeys = true }): BackloggedGame? =
    try {
        val dto = json.decodeFromString(SharedGameDto.serializer(), gameJson)
        val game = dto.toGame()
        val backloggedDateTime = LocalDateTime.parse(backloggedDate)

        when (status) {
            STATUS_TO_PLAY -> {
                BackloggedGame.ToPlay(
                    id = gameId.toString(),
                    game = game,
                    backloggedDate = backloggedDateTime,
                )
            }

            STATUS_NOW_PLAYING -> {
                BackloggedGame.Playing(
                    id = gameId.toString(),
                    game = game,
                    backloggedDate = backloggedDateTime,
                    startedDate = startedDate?.let { LocalDateTime.parse(it) } ?: backloggedDateTime,
                )
            }

            STATUS_FINISHED -> {
                BackloggedGame.Finished(
                    id = gameId.toString(),
                    game = game,
                    backloggedDate = backloggedDateTime,
                    startedDate = startedDate?.let { LocalDateTime.parse(it) } ?: backloggedDateTime,
                    finishedDate = finishedDate?.let { LocalDateTime.parse(it) } ?: backloggedDateTime,
                )
            }

            else -> {
                null
            }
        }
    } catch (_: Exception) {
        null
    }

fun BackloggedGameEntity.toDto(): BackloggedGameDto =
    BackloggedGameDto(
        id = gameId.toString(),
        userId = userId,
        gameId = gameId,
        status = status,
        backloggedDate = backloggedDate,
        startedDate = startedDate,
        finishedDate = finishedDate,
        gameData = gameJson,
        updatedAt = lastModifiedAt,
    )

fun BackloggedGameDto.toEntity(syncStatus: SyncStatus = SyncStatus.SYNCED): BackloggedGameEntity =
    BackloggedGameEntity(
        gameId = gameId,
        status = status,
        backloggedDate = backloggedDate,
        startedDate = startedDate,
        finishedDate = finishedDate,
        gameJson = gameData ?: "",
        syncStatus = syncStatus.name,
        userId = userId,
        lastModifiedAt = updatedAt ?: 0L,
    )

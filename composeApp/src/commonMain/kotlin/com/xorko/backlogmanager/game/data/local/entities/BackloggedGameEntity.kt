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
const val STATUS_DROPPED = "DROPPED"

@Entity(tableName = "backlogged_games")
data class BackloggedGameEntity(
    @PrimaryKey val gameId: Long,
    val status: String,
    val backloggedDate: String,
    val startedDate: String? = null,
    val finishedDate: String? = null,
    val droppedDate: String? = null,
    val resumedDate: String? = null,
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
                droppedDate = null,
                resumedDate = null,
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
                droppedDate = droppedDate?.toString(),
                resumedDate = resumedDate?.toString(),
                gameJson = gameJsonString,
                syncStatus = syncStatus.name,
                userId = userId,
                lastModifiedAt = nowMillis,
            )
        }

        is BackloggedGame.Dropped -> {
            BackloggedGameEntity(
                gameId = game.id,
                status = STATUS_DROPPED,
                backloggedDate = backloggedDate.toString(),
                startedDate = startedDate.toString(),
                finishedDate = null,
                droppedDate = droppedDate?.toString(),
                resumedDate = null,
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
                droppedDate = droppedDate?.toString(),
                resumedDate = resumedDate?.toString(),
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

        fun parseDate(dateStr: String?): LocalDateTime? =
            dateStr?.takeIf { it.isNotBlank() && it != "null" }?.let { LocalDateTime.parse(it) }

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
                    startedDate = parseDate(startedDate) ?: backloggedDateTime,
                    droppedDate = parseDate(droppedDate),
                    resumedDate = parseDate(resumedDate),
                )
            }

            STATUS_FINISHED -> {
                BackloggedGame.Finished(
                    id = gameId.toString(),
                    game = game,
                    backloggedDate = backloggedDateTime,
                    startedDate = parseDate(startedDate) ?: backloggedDateTime,
                    finishedDate = parseDate(finishedDate) ?: backloggedDateTime,
                    droppedDate = parseDate(droppedDate),
                    resumedDate = parseDate(resumedDate),
                )
            }

            STATUS_DROPPED -> {
                BackloggedGame.Dropped(
                    id = gameId.toString(),
                    game = game,
                    backloggedDate = backloggedDateTime,
                    startedDate = parseDate(startedDate) ?: backloggedDateTime,
                    droppedDate = parseDate(droppedDate),
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
        droppedDate = droppedDate,
        resumedDate = resumedDate,
        updatedAt = lastModifiedAt,
    )

fun BackloggedGameDto.toEntity(
    gameJson: String,
    syncStatus: SyncStatus = SyncStatus.SYNCED,
): BackloggedGameEntity =
    BackloggedGameEntity(
        gameId = gameId,
        status = status,
        backloggedDate = backloggedDate,
        startedDate = startedDate,
        finishedDate = finishedDate,
        droppedDate = droppedDate,
        resumedDate = resumedDate,
        gameJson = gameJson,
        syncStatus = syncStatus.name,
        userId = userId,
        lastModifiedAt = updatedAt ?: 0L,
    )

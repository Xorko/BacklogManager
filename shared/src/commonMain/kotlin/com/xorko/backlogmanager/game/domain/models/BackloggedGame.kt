package com.xorko.backlogmanager.game.domain.models

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.reflect.KClass
import kotlin.time.Clock

sealed class BackloggedGame {
    abstract val id: String
    abstract val game: Game

    data class ToPlay(
        override val id: String,
        override val game: Game,
        val backloggedDate: LocalDateTime,
    ) : BackloggedGame()

    data class NowPlaying(
        override val id: String,
        override val game: Game,
        val backloggedDate: LocalDateTime,
        val startedDate: LocalDateTime,
    ) : BackloggedGame()

    data class Finished(
        override val id: String,
        override val game: Game,
        val backloggedDate: LocalDateTime,
        val startedDate: LocalDateTime,
        val finishedDate: LocalDateTime,
    ) : BackloggedGame()
}

fun BackloggedGame.withStatus(status: KClass<out BackloggedGame>) =
    when (this) {
        is BackloggedGame.Finished -> withStatus(status)
        is BackloggedGame.NowPlaying -> withStatus(status)
        is BackloggedGame.ToPlay -> withStatus(status)
    }

fun BackloggedGame.ToPlay.withStatus(status: KClass<out BackloggedGame>): BackloggedGame {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    return when (status) {
        BackloggedGame.ToPlay::class -> {
            this
        }

        BackloggedGame.NowPlaying::class -> {
            BackloggedGame.NowPlaying(
                id = id,
                game = game,
                backloggedDate = backloggedDate,
                startedDate = now,
            )
        }

        BackloggedGame.Finished::class -> {
            BackloggedGame.Finished(
                id = id,
                game = game,
                backloggedDate = backloggedDate,
                startedDate = now,
                finishedDate = now,
            )
        }

        else -> {
            throw IllegalArgumentException("Invalid status")
        }
    }
}

fun BackloggedGame.NowPlaying.withStatus(status: KClass<out BackloggedGame>): BackloggedGame {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    return when (status) {
        BackloggedGame.ToPlay::class -> {
            BackloggedGame.ToPlay(
                id = id,
                game = game,
                backloggedDate = backloggedDate,
            )
        }

        BackloggedGame.NowPlaying::class -> {
            this
        }

        BackloggedGame.Finished::class -> {
            BackloggedGame.Finished(
                id = id,
                game = game,
                backloggedDate = backloggedDate,
                startedDate = startedDate,
                finishedDate = now,
            )
        }

        else -> {
            throw IllegalArgumentException("Invalid status")
        }
    }
}

fun BackloggedGame.Finished.withStatus(status: KClass<out BackloggedGame>) =
    when (status) {
        BackloggedGame.ToPlay::class -> {
            BackloggedGame.ToPlay(
                id = id,
                game = game,
                backloggedDate = backloggedDate,
            )
        }

        BackloggedGame.NowPlaying::class -> {
            BackloggedGame.NowPlaying(
                id = id,
                game = game,
                backloggedDate = backloggedDate,
                startedDate = startedDate,
            )
        }

        BackloggedGame.Finished::class -> {
            this
        }

        else -> {
            throw IllegalArgumentException("Invalid status")
        }
    }

package com.xorko.backlogmanager.game.domain.models

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.reflect.KClass
import kotlin.time.Clock

sealed interface BackloggedGame {
    val id: String
    val game: Game
    val backloggedDate: LocalDateTime

    sealed interface Started : BackloggedGame {
        val startedDate: LocalDateTime
    }

    sealed interface Droppable : BackloggedGame {
        val droppedDate: LocalDateTime?
        val resumedDate: LocalDateTime?
    }

    data class ToPlay(
        override val id: String,
        override val game: Game,
        override val backloggedDate: LocalDateTime,
    ) : BackloggedGame

    data class Playing(
        override val id: String,
        override val game: Game,
        override val backloggedDate: LocalDateTime,
        override val startedDate: LocalDateTime,
        override val droppedDate: LocalDateTime?,
        override val resumedDate: LocalDateTime?,
    ) : Started,
        Droppable

    data class Finished(
        override val id: String,
        override val game: Game,
        override val backloggedDate: LocalDateTime,
        override val startedDate: LocalDateTime,
        override val droppedDate: LocalDateTime?,
        override val resumedDate: LocalDateTime?,
        val finishedDate: LocalDateTime,
    ) : Started,
        Droppable

    data class Dropped(
        override val id: String,
        override val game: Game,
        override val backloggedDate: LocalDateTime,
        override val startedDate: LocalDateTime,
        val droppedDate: LocalDateTime,
    ) : Started
}

fun BackloggedGame.withStatus(status: KClass<out BackloggedGame>): BackloggedGame =
    when (this) {
        is BackloggedGame.Finished -> {
            withStatus(status)
        }

        is BackloggedGame.Playing -> {
            withStatus(status)
        }

        is BackloggedGame.ToPlay -> {
            withStatus(status)
        }

        is BackloggedGame.Dropped -> {
            when (status) {
                BackloggedGame.Dropped::class -> {
                    this
                }

                BackloggedGame.Playing::class -> {
                    resume()
                }

                BackloggedGame.ToPlay::class -> {
                    BackloggedGame.ToPlay(
                        id = id,
                        game = game,
                        backloggedDate = backloggedDate,
                    )
                }

                else -> {
                    throw IllegalArgumentException("Invalid status")
                }
            }
        }
    }

fun BackloggedGame.ToPlay.withStatus(status: KClass<out BackloggedGame>): BackloggedGame {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    return when (status) {
        BackloggedGame.ToPlay::class -> {
            this
        }

        BackloggedGame.Playing::class -> {
            BackloggedGame.Playing(
                id = id,
                game = game,
                backloggedDate = backloggedDate,
                startedDate = now,
                droppedDate = null,
                resumedDate = null,
            )
        }

        BackloggedGame.Finished::class -> {
            BackloggedGame.Finished(
                id = id,
                game = game,
                backloggedDate = backloggedDate,
                startedDate = now,
                finishedDate = now,
                droppedDate = null,
                resumedDate = null,
            )
        }

        BackloggedGame.Dropped::class -> {
            BackloggedGame.Dropped(
                id = id,
                game = game,
                backloggedDate = backloggedDate,
                startedDate = now,
                droppedDate = now,
            )
        }

        else -> {
            throw IllegalArgumentException("Invalid status")
        }
    }
}

fun BackloggedGame.Playing.withStatus(status: KClass<out BackloggedGame>): BackloggedGame {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    return when (status) {
        BackloggedGame.ToPlay::class -> {
            BackloggedGame.ToPlay(
                id = id,
                game = game,
                backloggedDate = backloggedDate,
            )
        }

        BackloggedGame.Playing::class -> {
            this
        }

        BackloggedGame.Finished::class -> {
            BackloggedGame.Finished(
                id = id,
                game = game,
                backloggedDate = backloggedDate,
                startedDate = startedDate,
                finishedDate = now,
                droppedDate = droppedDate,
                resumedDate = resumedDate,
            )
        }

        BackloggedGame.Dropped::class -> {
            BackloggedGame.Dropped(
                id = id,
                game = game,
                backloggedDate = backloggedDate,
                startedDate = startedDate,
                droppedDate = now,
            )
        }

        else -> {
            throw IllegalArgumentException("Invalid status")
        }
    }
}

fun BackloggedGame.Finished.withStatus(status: KClass<out BackloggedGame>): BackloggedGame =
    when (status) {
        BackloggedGame.ToPlay::class -> {
            BackloggedGame.ToPlay(
                id = id,
                game = game,
                backloggedDate = backloggedDate,
            )
        }

        BackloggedGame.Playing::class -> {
            BackloggedGame.Playing(
                id = id,
                game = game,
                backloggedDate = backloggedDate,
                startedDate = startedDate,
                droppedDate = droppedDate,
                resumedDate = resumedDate,
            )
        }

        BackloggedGame.Finished::class -> {
            this
        }

        else -> {
            throw IllegalArgumentException("Invalid status")
        }
    }

fun BackloggedGame.Dropped.resume(): BackloggedGame.Playing {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    return BackloggedGame.Playing(
        id = id,
        game = game,
        backloggedDate = backloggedDate,
        startedDate = startedDate,
        droppedDate = droppedDate,
        resumedDate = now,
    )
}

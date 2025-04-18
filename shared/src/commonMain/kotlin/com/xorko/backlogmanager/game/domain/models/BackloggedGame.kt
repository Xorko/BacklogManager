package com.xorko.backlogmanager.game.domain.models

import kotlinx.datetime.LocalDateTime

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

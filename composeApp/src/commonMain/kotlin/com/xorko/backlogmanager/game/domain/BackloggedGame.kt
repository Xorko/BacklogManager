package com.xorko.backlogmanager.game.domain

import kotlinx.datetime.LocalDateTime

sealed class BackloggedGame {
    abstract val game: Game

    data class ToPlay(
        override val game: Game,
        val backloggedDate: LocalDateTime,
    ) : BackloggedGame()

    data class NowPlaying(
        override val game: Game,
        val backloggedDate: LocalDateTime,
        val startedDate: LocalDateTime,
    ) : BackloggedGame()

    data class Finished(
        override val game: Game,
        val backloggedDate: LocalDateTime,
        val startedDate: LocalDateTime,
        val finishedDate: LocalDateTime,
    ) : BackloggedGame()
}

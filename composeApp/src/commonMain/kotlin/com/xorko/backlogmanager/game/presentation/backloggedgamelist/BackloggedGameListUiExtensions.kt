package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.backlogged_game_added_on
import backlogmanager.composeapp.generated.resources.backlogged_game_finished_on
import backlogmanager.composeapp.generated.resources.backlogged_game_started_on
import backlogmanager.composeapp.generated.resources.backlogged_game_status_finished
import backlogmanager.composeapp.generated.resources.backlogged_game_status_now_playing
import backlogmanager.composeapp.generated.resources.backlogged_game_status_to_play
import com.xorko.backlogmanager.core.presentation.UiText
import com.xorko.backlogmanager.game.domain.BackloggedGame
import com.xorko.backlogmanager.game.domain.BackloggedGame.Finished
import com.xorko.backlogmanager.game.domain.BackloggedGame.NowPlaying
import com.xorko.backlogmanager.game.domain.BackloggedGame.ToPlay
import kotlinx.datetime.LocalDateTime

fun BackloggedGame.getStatus(): UiText =
    when (this) {
        is ToPlay -> UiText.StringResourceId(Res.string.backlogged_game_status_to_play)
        is NowPlaying -> UiText.StringResourceId(Res.string.backlogged_game_status_now_playing)
        is Finished -> UiText.StringResourceId(Res.string.backlogged_game_status_finished)
    }

fun BackloggedGame.getLongStatus(): UiText =
    when (this) {
        is ToPlay -> getLongStatus()
        is NowPlaying -> getLongStatus()
        is Finished -> getLongStatus()
    }

fun BackloggedGame.formatDevelopersAndPublishers(): String = "${formatDeveloperList()} / ${formatPublisherList()}"

fun BackloggedGame.formatPublisherList(): String = game.publishers.joinToString { it.name }

fun BackloggedGame.formatDeveloperList(): String = game.developers.joinToString { it.name }

private fun ToPlay.getLongStatus(): UiText =
    UiText.StringResourceId(
        Res.string.backlogged_game_added_on,
        arrayOf(backloggedDate.toLongDateFormat()),
    )

private fun NowPlaying.getLongStatus(): UiText =
    UiText.StringResourceId(
        Res.string.backlogged_game_started_on,
        arrayOf(backloggedDate.toLongDateFormat()),
    )

private fun Finished.getLongStatus(): UiText =
    UiText.StringResourceId(
        Res.string.backlogged_game_finished_on,
        arrayOf(backloggedDate.toLongDateFormat()),
    )

expect fun LocalDateTime.toLongDateFormat(): String

package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.backlogged_game_added_on
import backlogmanager.composeapp.generated.resources.backlogged_game_finished_on
import backlogmanager.composeapp.generated.resources.backlogged_game_started_on
import backlogmanager.composeapp.generated.resources.backlogged_game_status_all
import backlogmanager.composeapp.generated.resources.backlogged_game_status_finished
import backlogmanager.composeapp.generated.resources.backlogged_game_status_now_playing
import backlogmanager.composeapp.generated.resources.backlogged_game_status_to_play
import com.xorko.backlogmanager.core.presentation.UiText
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.Finished
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.NowPlaying
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.ToPlay
import com.xorko.backlogmanager.game.domain.models.Game
import kotlinx.datetime.LocalDateTime
import kotlin.reflect.KClass

fun BackloggedGame.getStatus(): UiText =
    when (this) {
        is ToPlay -> UiText.StringResourceId(Res.string.backlogged_game_status_to_play)
        is NowPlaying -> UiText.StringResourceId(Res.string.backlogged_game_status_now_playing)
        is Finished -> UiText.StringResourceId(Res.string.backlogged_game_status_finished)
    }

fun KClass<out BackloggedGame>?.getStatus(): UiText =
    when (this) {
        null -> UiText.StringResourceId(Res.string.backlogged_game_status_all)
        ToPlay::class -> UiText.StringResourceId(Res.string.backlogged_game_status_to_play)
        NowPlaying::class -> UiText.StringResourceId(Res.string.backlogged_game_status_now_playing)
        Finished::class -> UiText.StringResourceId(Res.string.backlogged_game_status_finished)
        else -> error("Unknown BackloggedGame type: $this")
    }

fun BackloggedGame.getLongStatus(): UiText =
    when (this) {
        is ToPlay -> getLongStatus()
        is NowPlaying -> getLongStatus()
        is Finished -> getLongStatus()
    }

fun Game.formatDevelopersAndPublishers(): String = "${formatDeveloperList()} / ${formatPublisherList()}"

fun Game.formatPublisherList(): String = publishers.joinToString { it.name }

fun Game.formatDeveloperList(): String = developers.joinToString { it.name }

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

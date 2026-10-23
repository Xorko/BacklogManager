package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.backlogged_game_added_on_format
import backlogmanager.composeapp.generated.resources.backlogged_game_finished_on_format
import backlogmanager.composeapp.generated.resources.backlogged_game_started_on_format
import backlogmanager.composeapp.generated.resources.backlogged_game_status_all
import backlogmanager.composeapp.generated.resources.backlogged_game_status_finished
import backlogmanager.composeapp.generated.resources.backlogged_game_status_playing
import backlogmanager.composeapp.generated.resources.backlogged_game_status_to_play
import com.xorko.backlogmanager.core.presentation.UiText
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.Finished
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.Playing
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.ToPlay
import com.xorko.backlogmanager.utils.toLongDateFormat
import kotlin.reflect.KClass

fun BackloggedGame.getStatus(): UiText =
    when (this) {
        is ToPlay -> UiText.StringResourceId(Res.string.backlogged_game_status_to_play)
        is Playing -> UiText.StringResourceId(Res.string.backlogged_game_status_playing)
        is Finished -> UiText.StringResourceId(Res.string.backlogged_game_status_finished)
    }

fun KClass<out BackloggedGame>?.getStatus(): UiText =
    when (this) {
        null -> UiText.StringResourceId(Res.string.backlogged_game_status_all)
        ToPlay::class -> UiText.StringResourceId(Res.string.backlogged_game_status_to_play)
        Playing::class -> UiText.StringResourceId(Res.string.backlogged_game_status_playing)
        Finished::class -> UiText.StringResourceId(Res.string.backlogged_game_status_finished)
        else -> error("Unknown BackloggedGame type: $this")
    }

fun BackloggedGame.getLongStatus(): UiText =
    when (this) {
        is ToPlay -> getLongStatus()
        is Playing -> getLongStatus()
        is Finished -> getLongStatus()
    }

private fun ToPlay.getLongStatus(): UiText =
    UiText.StringResourceId(
        Res.string.backlogged_game_added_on_format,
        arrayOf(backloggedDate.toLongDateFormat()),
    )

private fun Playing.getLongStatus(): UiText =
    UiText.StringResourceId(
        Res.string.backlogged_game_started_on_format,
        arrayOf(startedDate.toLongDateFormat()),
    )

private fun Finished.getLongStatus(): UiText =
    UiText.StringResourceId(
        Res.string.backlogged_game_finished_on_format,
        arrayOf(finishedDate.toLongDateFormat()),
    )

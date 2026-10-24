package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.backlogged_game_status_all
import backlogmanager.composeapp.generated.resources.backlogged_game_status_dropped
import backlogmanager.composeapp.generated.resources.backlogged_game_status_finished
import backlogmanager.composeapp.generated.resources.backlogged_game_status_playing
import backlogmanager.composeapp.generated.resources.backlogged_game_status_to_play
import com.xorko.backlogmanager.core.presentation.UiText
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.Dropped
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.Finished
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.Playing
import com.xorko.backlogmanager.game.domain.models.BackloggedGame.ToPlay
import kotlin.reflect.KClass

fun BackloggedGame.getStatus(): UiText =
    when (this) {
        is ToPlay -> UiText.StringResourceId(Res.string.backlogged_game_status_to_play)
        is Playing -> UiText.StringResourceId(Res.string.backlogged_game_status_playing)
        is Finished -> UiText.StringResourceId(Res.string.backlogged_game_status_finished)
        is Dropped -> UiText.StringResourceId(Res.string.backlogged_game_status_dropped)
    }

fun KClass<out BackloggedGame>?.getStatus(): UiText =
    when (this) {
        null -> UiText.StringResourceId(Res.string.backlogged_game_status_all)
        ToPlay::class -> UiText.StringResourceId(Res.string.backlogged_game_status_to_play)
        Playing::class -> UiText.StringResourceId(Res.string.backlogged_game_status_playing)
        Finished::class -> UiText.StringResourceId(Res.string.backlogged_game_status_finished)
        Dropped::class -> UiText.StringResourceId(Res.string.backlogged_game_status_dropped)
        else -> error("Unknown BackloggedGame type: $this")
    }

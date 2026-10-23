package com.xorko.backlogmanager.game.presentation.gamedetails

import com.xorko.backlogmanager.core.presentation.UiText
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.domain.models.Game

data class GameDetailsState(
    val isLoading: Boolean = true,
    val game: Game? = null,
    val backloggedGame: BackloggedGame? = null,
    val errorMessage: UiText? = null,
)

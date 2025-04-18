package com.xorko.backlogmanager.game.presentation.gamelist

import com.xorko.backlogmanager.game.domain.models.Game

sealed interface GameListActions {
    data class OnSearchQueryChange(
        val query: String,
    ) : GameListActions

    data class OnGameClicked(
        val game: Game,
    ) : GameListActions
}

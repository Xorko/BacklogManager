package com.xorko.backlogmanager.game.presentation.gamelist

import com.xorko.backlogmanager.core.presentation.UiText
import com.xorko.backlogmanager.game.domain.models.Game

data class GameListState(
    val searchResults: List<Game> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val isLoadingMoreGames: Boolean = false,
    val errorMessage: UiText? = null,
)

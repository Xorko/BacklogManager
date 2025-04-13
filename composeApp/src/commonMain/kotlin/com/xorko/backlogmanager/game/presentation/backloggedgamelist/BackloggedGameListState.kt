package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import com.xorko.backlogmanager.core.presentation.UiText
import com.xorko.backlogmanager.game.domain.Game

data class BackloggedGameListState(
    val searchQuery: String = "",
    val searchResults: List<Game> = emptyList(),
    val backloggedGames: List<Game> = emptyList(),
    val selectedFilterIndex: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
)

package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import com.xorko.backlogmanager.core.presentation.UiText
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import kotlinx.datetime.minus
import kotlin.reflect.KClass

data class BackloggedGameListState(
    val searchQuery: String = "",
    val searchResults: List<BackloggedGame> = emptyList(),
    val selectedFilter: KClass<out BackloggedGame>? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: UiText? = null,
)

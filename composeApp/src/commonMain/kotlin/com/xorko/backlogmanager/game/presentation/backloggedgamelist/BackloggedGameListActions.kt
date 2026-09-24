package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import kotlin.reflect.KClass

sealed interface BackloggedGameListActions {
    data class OnSearchQueryChange(
        val query: String,
    ) : BackloggedGameListActions

    data class OnBackloggedGameClicked(
        val game: BackloggedGame,
    ) : BackloggedGameListActions

    data class OnFilterSelected(
        val index: KClass<out BackloggedGame>?,
    ) : BackloggedGameListActions

    data object OnRefresh : BackloggedGameListActions
}

package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.game.domain.BacklogRepository
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class BackloggedGameListViewModel(
    private val backlogRepository: BacklogRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(BackloggedGameListState())
    val state = _state.asStateFlow()

    init {
        syncBacklog()

        combine(
            backlogRepository.getBackloggedGames(),
            _state.map { it.searchQuery }.distinctUntilChanged(),
            _state.map { it.selectedFilter }.distinctUntilChanged(),
        ) { games, query, filter ->
            val filteredByQuery =
                if (query.isBlank()) {
                    games
                } else {
                    games.filter { it.game.name.contains(query, ignoreCase = true) }
                }

            val filteredByStatus =
                if (filter == null) {
                    filteredByQuery
                } else {
                    filteredByQuery.filter { filter.isInstance(it) }
                }

            val counts = filteredByQuery.groupingBy { it::class }.eachCount()

            BacklogSnapshot(
                searchResults = filteredByStatus,
                statusCounts = counts,
            )
        }.onEach { snapshot ->
            _state.update {
                it.copy(
                    searchResults = snapshot.searchResults,
                    statusCounts = snapshot.statusCounts,
                    isLoading = false,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: BackloggedGameListActions) {
        when (action) {
            is BackloggedGameListActions.OnBackloggedGameClicked -> {
            }

            is BackloggedGameListActions.OnRemoveBackloggedGame -> {
                removeFromBacklog(action.game.game.id)
            }

            is BackloggedGameListActions.OnFilterSelected -> {
                _state.update {
                    it.copy(selectedFilter = action.index)
                }
                Logger.v("New state value: ${state.value}", tag = this::class.simpleName)
            }

            is BackloggedGameListActions.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
                Logger.v("New state value: ${state.value}", tag = this::class.simpleName)
            }

            BackloggedGameListActions.OnRefresh -> {
                syncBacklog(isRefresh = true)
            }
        }
    }

    private fun removeFromBacklog(gameId: Long) {
        viewModelScope.launch {
            backlogRepository.removeFromBacklog(gameId).fold(
                ifLeft = {
                    Logger.d("Failed to remove from backlog")
                },
                ifRight = {
                    Logger.d("Game removed from backlog")
                },
            )
        }
    }

    private fun syncBacklog(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _state.update { it.copy(isRefreshing = true) }
            }

            backlogRepository.sync().fold(
                ifLeft = { error ->
                    Logger.e("Sync failed: $error", tag = this::class.simpleName)
                },
                ifRight = {
                    Logger.d("Sync succeeded", tag = this::class.simpleName)
                },
            )

            if (isRefresh) {
                _state.update { it.copy(isRefreshing = false) }
            }
        }
    }
}

/**
 * Data holder for the combine result in [BackloggedGameListViewModel].
 *
 * @property searchResults - List of games that match the search query.
 * @property statusCounts - Map of the number of games for each status.
 */
private data class BacklogSnapshot(
    val searchResults: List<BackloggedGame>,
    val statusCounts: Map<KClass<out BackloggedGame>, Int>,
)

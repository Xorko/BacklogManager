package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.game.domain.BacklogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

            filteredByStatus
        }.onEach { searchResults ->
            _state.update {
                it.copy(
                    searchResults = searchResults,
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
                syncBacklog()
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

    private fun syncBacklog() {
        viewModelScope.launch {
            backlogRepository.sync()
        }
    }
}

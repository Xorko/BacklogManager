package com.xorko.backlogmanager.game.presentation.gamelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xorko.backlogmanager.core.presentation.toUiText
import com.xorko.backlogmanager.game.domain.GameRepository
import com.xorko.backlogmanager.game.domain.models.Game
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameListViewModel(
    private val gameRepository: GameRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(GameListState())
    val state = _state.asStateFlow()

    private var cachedGames: List<Game> = emptyList()
    private var searchJob: Job? = null

    init {
        observeSearchQuery()
    }

    fun onAction(action: GameListActions) {
        when (action) {
            is GameListActions.OnGameClicked -> {
            }

            is GameListActions.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500L)
            .onEach { query ->
                when {
                    query.isBlank() -> {
                        _state.update {
                            it.copy(
                                errorMessage = null,
                                searchResults = cachedGames,
                            )
                        }
                    }

                    query.length >= 2 -> {
                        searchJob?.cancel()
                        searchJob = searchGames(query)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun searchGames(query: String) =
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            gameRepository
                .searchGames(query)
                .fold(
                    ifLeft = { error ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = error.toUiText(),
                            )
                        }
                    },
                    ifRight = { searchResults ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                searchResults = searchResults,
                                errorMessage = null,
                            )
                        }
                    },
                )
        }
}

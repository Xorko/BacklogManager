package com.xorko.backlogmanager.game.presentation.gamelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.core.presentation.toUiText
import com.xorko.backlogmanager.game.domain.GameRepository
import com.xorko.backlogmanager.game.domain.models.Game
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class GameListViewModel(
    private val gameRepository: GameRepository,
) : ViewModel() {
    private companion object {
        private const val GAMES_PER_PAGE = 20
    }

    private val _state = MutableStateFlow(GameListState())
    val state =
        _state
            .onStart {
                Logger.d("GameListViewModel state flow started", tag = this::class.simpleName)
                observeSearchQuery()
                fetchGamesJob = fetchGames()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = GameListState(),
            )

    private var currentOffset = 0
    private var cachedGames: List<Game> = emptyList()

    private var fetchGamesJob: Job? = null
    private var searchJob: Job? = null

    fun onAction(action: GameListActions) {
        when (action) {
            is GameListActions.OnGameClicked -> {
            }

            is GameListActions.OnSearchQueryChange -> {
                handleOnSearchQueryChange(action.query)
            }

            is GameListActions.OnListEndAlmostReached -> {
                handleOnListEndAlmostReached()
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500.milliseconds)
            .onEach { query ->
                Logger.d("New query: $query", tag = this::class.simpleName)
                when {
                    query.isBlank() -> {
                        _state.update {
                            it.copy(
                                errorMessage = null,
                                searchResults = cachedGames,
                            )
                        }
                        Logger.v("New state value: ${state.value}", tag = this::class.simpleName)
                    }

                    query.length >= 2 -> {
                        searchJob?.cancel().also {
                            Logger.d(
                                "Cancelling search job",
                                tag = this::class.simpleName,
                            )
                        }
                        searchJob = searchGames(query)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun handleOnSearchQueryChange(query: String) {
        _state.update {
            it.copy(searchQuery = query)
        }
        Logger.v("New state value: ${state.value}", tag = this::class.simpleName)
    }

    private fun handleOnListEndAlmostReached(): Unit =
        with(state.value) {
            if (isLoading || isLoadingMoreGames || searchQuery.isNotBlank()) return

            fetchGamesJob?.cancel()
            fetchGamesJob = fetchMoreGames()
            Logger.v("New state value: ${state.value}", tag = this::class.simpleName)
        }

    private fun searchGames(query: String): Job =
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            Logger.v("New state value: ${state.value}", tag = this::class.simpleName)
            gameRepository
                .searchGames(query)
                .fold(
                    ifLeft = { error ->
                        Logger.e("Error searching games: $error", tag = this::class.simpleName)
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = error.toUiText(),
                            )
                        }
                        Logger.v("New state value: ${state.value}", tag = this::class.simpleName)
                    },
                    ifRight = { searchResults ->
                        Logger.v("Search results: $searchResults", tag = this::class.simpleName)
                        _state.update {
                            it.copy(
                                isLoading = false,
                                searchResults = searchResults,
                                errorMessage = null,
                            )
                        }
                        Logger.v("New state value: ${state.value}", tag = this::class.simpleName)
                    },
                )
        }

    private fun fetchGames(): Job =
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            gameRepository
                .getGames()
                .fold(
                    ifLeft = { error ->
                        Logger.e("Error fetching games: $error", tag = this::class.simpleName)
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = error.toUiText(),
                            )
                        }
                        Logger.v("New state value: ${state.value}", tag = this::class.simpleName)
                    },
                    ifRight = { games ->
                        Logger.v("Fetched games: $games", tag = this::class.simpleName)
                        cachedGames = games
                        _state.update {
                            it.copy(
                                isLoading = false,
                                searchResults = games,
                                errorMessage = null,
                            )
                        }
                        Logger.v("New state value: ${state.value}", tag = this::class.simpleName)
                    },
                )
        }

    private fun fetchMoreGames(): Job =
        viewModelScope.launch {
            _state.update { it.copy(isLoadingMoreGames = true) }
            gameRepository
                .getGames(
                    offset = state.value.searchResults.size,
                    limit = GAMES_PER_PAGE,
                ).fold(
                    ifLeft = { error ->
                        Logger.e("Error fetching games: $error", tag = this::class.simpleName)
                        _state.update {
                            it.copy(
                                isLoadingMoreGames = false,
                                errorMessage = error.toUiText(),
                            )
                        }
                        Logger.v("New state value: ${state.value}", tag = this::class.simpleName)
                    },
                    ifRight = {
                        Logger.v("Fetched games: $it", tag = this::class.simpleName)
                        currentOffset += it.size
                        cachedGames = cachedGames + it
                        _state.update {
                            it.copy(
                                isLoadingMoreGames = false,
                                searchResults = cachedGames,
                                errorMessage = null,
                            )
                        }
                        Logger.v("New state value: ${state.value}", tag = this::class.simpleName)
                    },
                )
        }
}

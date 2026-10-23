package com.xorko.backlogmanager.game.presentation.gamedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.add_to_backlog_failed
import backlogmanager.composeapp.generated.resources.add_to_backlog_success
import backlogmanager.composeapp.generated.resources.error_unknown
import backlogmanager.composeapp.generated.resources.remove_from_backlog_failed
import backlogmanager.composeapp.generated.resources.remove_from_backlog_success
import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.core.presentation.SnackbarController
import com.xorko.backlogmanager.core.presentation.SnackbarEvent
import com.xorko.backlogmanager.core.presentation.UiText
import com.xorko.backlogmanager.core.presentation.toUiText
import com.xorko.backlogmanager.game.domain.BacklogRepository
import com.xorko.backlogmanager.game.domain.GameRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameDetailsViewModel(
    private val gameRepository: GameRepository,
    private val backlogRepository: BacklogRepository,
    gameId: Long? = null,
) : ViewModel() {
    private val _state = MutableStateFlow(GameDetailsState())
    val state =
        _state
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = _state.value,
            )

    private var backlogObservationJob: Job? = null

    init {
        gameId?.let { id ->
            observeBacklogState(id)
            fetchGameDetails(id)
        } ?: run {
            Logger.e("Missing gameId")
            _state.update {
                it.copy(
                    isLoading = false,
                    errorMessage = UiText.StringResourceId(Res.string.error_unknown),
                )
            }
        }
    }

    private fun observeBacklogState(gameId: Long) {
        backlogObservationJob?.cancel()
        backlogObservationJob =
            viewModelScope.launch {
                backlogRepository.isGameInBacklog(gameId).collect { inBacklog ->
                    _state.update { it.copy(isInBacklog = inBacklog) }
                }
            }
    }

    fun onAction(action: GameDetailsActions) =
        when (action) {
            is GameDetailsActions.OnAddToBacklog -> toggleBacklog()
            else -> Logger.d("Unhandled action")
        }

    private fun toggleBacklog() {
        val currentGame = _state.value.game ?: return
        val currentlyInBacklog = _state.value.isInBacklog

        viewModelScope.launch {
            if (currentlyInBacklog) {
                backlogRepository.removeFromBacklog(currentGame.id).fold(
                    ifLeft = {
                        Logger.d("Failed to remove from backlog")
                        SnackbarController.sendEvent(
                            SnackbarEvent(
                                message = UiText.StringResourceId(Res.string.remove_from_backlog_failed),
                            ),
                        )
                    },
                    ifRight = {
                        Logger.d("Game removed from backlog")
                        SnackbarController.sendEvent(
                            SnackbarEvent(
                                message = UiText.StringResourceId(Res.string.remove_from_backlog_success),
                            ),
                        )
                    },
                )
            } else {
                backlogRepository.addToBacklog(currentGame).fold(
                    ifLeft = {
                        Logger.d("Failed to add to backlog")
                        SnackbarController.sendEvent(
                            SnackbarEvent(
                                message = UiText.StringResourceId(Res.string.add_to_backlog_failed),
                            ),
                        )
                    },
                    ifRight = {
                        Logger.d("Game added to backlog")
                        SnackbarController.sendEvent(
                            SnackbarEvent(
                                message = UiText.StringResourceId(Res.string.add_to_backlog_success),
                            ),
                        )
                    },
                )
            }
        }
    }

    private fun fetchGameDetails(id: Long): Job =
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            gameRepository.getGameById(id).fold(
                ifLeft = { error ->
                    Logger.e("Error fetching game details: $error")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.toUiText(),
                        )
                    }
                },
                ifRight = { game ->
                    Logger.d("Game details fetched: $game")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            game = game,
                            errorMessage = null,
                        )
                    }
                },
            )
        }
}

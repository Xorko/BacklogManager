package com.xorko.backlogmanager.game.presentation.gamedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.error_unknown
import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.core.presentation.UiText
import com.xorko.backlogmanager.core.presentation.toUiText
import com.xorko.backlogmanager.game.domain.BacklogRepository
import com.xorko.backlogmanager.game.domain.GameRepository
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.domain.models.withStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

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
                backlogRepository.getBackloggedGameById(gameId).collect { backloggedGame ->
                    _state.update { it.copy(backloggedGame = backloggedGame) }
                }
            }
    }

    fun onAction(action: GameDetailsActions) =
        when (action) {
            is GameDetailsActions.OnAddToBacklog -> addToBacklog()
            is GameDetailsActions.OnRemoveFromBacklog -> removeFromBacklog()
            is GameDetailsActions.OnStatusSelected -> updateBacklogStatus(status = action.status)
            else -> Logger.d("Unhandled action")
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

    private fun addToBacklog() {
        val currentGame = _state.value.game ?: return

        viewModelScope.launch {
            backlogRepository.addToBacklog(currentGame).fold(
                ifLeft = {
                    Logger.d("Failed to add to backlog")
                },
                ifRight = {
                    Logger.d("Game added to backlog")
                },
            )
        }
    }

    private fun removeFromBacklog() {
        val currentGame = _state.value.game ?: return

        viewModelScope.launch {
            backlogRepository.removeFromBacklog(currentGame.id).fold(
                ifLeft = {
                    Logger.d("Failed to remove from backlog")
                },
                ifRight = {
                    Logger.d("Game removed from backlog")
                },
            )
        }
    }

    private fun updateBacklogStatus(status: KClass<out BackloggedGame>) {
        val backloggedGame = _state.value.backloggedGame ?: return

        viewModelScope.launch {
            backlogRepository.updateBackloggedGameStatus(backloggedGame.withStatus(status)).fold(
                ifLeft = {
                    Logger.d("Failed to update backlog status")
                },
                ifRight = {
                    Logger.d("Backlog status updated")
                },
            )
        }
    }
}

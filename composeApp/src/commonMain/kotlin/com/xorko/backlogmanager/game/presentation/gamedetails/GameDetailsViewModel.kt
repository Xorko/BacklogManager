package com.xorko.backlogmanager.game.presentation.gamedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.error_unknown
import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.core.presentation.SnackbarController
import com.xorko.backlogmanager.core.presentation.SnackbarEvent
import com.xorko.backlogmanager.core.presentation.UiText
import com.xorko.backlogmanager.core.presentation.toUiText
import com.xorko.backlogmanager.game.domain.GameRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val gameRepository: GameRepository,
) : ViewModel() {
    companion object {
        const val GAME_ID = "gameId"
    }

    private val _state = MutableStateFlow(GameDetailsState())
    val state =
        _state
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = _state.value,
            )

    init {
        val gameId = savedStateHandle.get<Long>(GAME_ID)

        gameId?.let {
            fetchGameDetails(gameId)
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

    fun onAction(action: GameDetailsActions) =
        when (action) {
            is GameDetailsActions.OnAddToBacklog ->
                viewModelScope.launch {
                    SnackbarController.sendEvent(SnackbarEvent(message = "Not implemented yet"))
                }
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
}

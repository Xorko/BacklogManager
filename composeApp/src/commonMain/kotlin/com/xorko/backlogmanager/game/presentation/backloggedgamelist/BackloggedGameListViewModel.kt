package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import androidx.lifecycle.ViewModel
import com.xorko.backlogmanager.core.domain.logging.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BackloggedGameListViewModel : ViewModel() {
    private val _state = MutableStateFlow(BackloggedGameListState())
    val state = _state.asStateFlow()

    fun onAction(action: BackloggedGameListActions) {
        when (action) {
            is BackloggedGameListActions.OnBackloggedGameClicked -> {
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
        }
    }
}

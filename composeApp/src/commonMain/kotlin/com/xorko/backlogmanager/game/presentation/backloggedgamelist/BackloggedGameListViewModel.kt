package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import androidx.lifecycle.ViewModel
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
            }

            is BackloggedGameListActions.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }
        }
    }
}

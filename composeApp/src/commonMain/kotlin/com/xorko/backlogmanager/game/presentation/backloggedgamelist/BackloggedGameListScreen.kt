package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xorko.backlogmanager.core.presentation.components.GameSearchBar
import com.xorko.backlogmanager.game.domain.BackloggedGame
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BackloggedGameListScreenRoot(
    viewModel: BackloggedGameListViewModel = koinViewModel(),
    onGameClick: (BackloggedGame) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BackloggedGameListScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is BackloggedGameListActions.OnBackloggedGameClicked -> onGameClick(action.game)
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
private fun BackloggedGameListScreen(
    state: BackloggedGameListState,
    onAction: (BackloggedGameListActions) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier.fillMaxSize().background(color = Color.White).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GameSearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = {
                onAction(BackloggedGameListActions.OnSearchQueryChange(it))
            },
            onImeSearch = {
                keyboardController?.hide()
            },
            modifier = Modifier.widthIn(max = 400.dp).padding(16.dp),
        )
    }
}

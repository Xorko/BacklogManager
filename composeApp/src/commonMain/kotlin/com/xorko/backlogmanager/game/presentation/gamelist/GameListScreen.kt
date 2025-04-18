package com.xorko.backlogmanager.game.presentation.gamelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xorko.backlogmanager.core.presentation.components.GameSearchBar
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.game.presentation.components.ErrorMessage
import com.xorko.backlogmanager.game.presentation.components.NoSearchResults
import com.xorko.backlogmanager.game.presentation.gamelist.components.GameList
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameListScreenRoot(
    viewModel: GameListViewModel = koinViewModel(),
    onGameClick: (Game) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GameListScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is GameListActions.OnGameClicked -> onGameClick(action.game)
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
fun GameListScreen(
    state: GameListState,
    onAction: (GameListActions) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val gamesLazyListState = rememberLazyListState()

    LaunchedEffect(state.searchResults) {
        gamesLazyListState.animateScrollToItem(0)
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GameSearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = {
                onAction(GameListActions.OnSearchQueryChange(it))
            },
            onImeSearch = {
                keyboardController?.hide()
            },
            modifier = Modifier.widthIn(max = 400.dp).padding(16.dp).fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(Dimens.spacingMedium))
        Surface(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        when {
                            state.errorMessage != null ->
                                ErrorMessage(
                                    message = state.errorMessage.asString(),
                                    modifier =
                                        Modifier.padding(
                                            Dimens.paddingMedium,
                                        ),
                                )

                            state.searchResults.isEmpty() -> NoSearchResults()

                            else ->
                                GameList(
                                    games = state.searchResults,
                                    onGameClicked = {
                                        onAction(
                                            GameListActions.OnGameClicked(
                                                it,
                                            ),
                                        )
                                    },
                                    scrollState = gamesLazyListState,
                                    modifier = Modifier.align(Alignment.TopStart),
                                )
                        }
                    }
                }
            }
        }
    }
}

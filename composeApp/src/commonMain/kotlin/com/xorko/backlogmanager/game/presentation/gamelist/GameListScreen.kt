package com.xorko.backlogmanager.game.presentation.gamelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.no_search_results
import backlogmanager.composeapp.generated.resources.search_hint
import com.xorko.backlogmanager.core.presentation.components.GameSearchBar
import com.xorko.backlogmanager.core.presentation.components.ScrollToTopFab
import com.xorko.backlogmanager.core.presentation.generateGame
import com.xorko.backlogmanager.core.presentation.shouldLoadMoreItems
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.game.presentation.components.ErrorMessage
import com.xorko.backlogmanager.game.presentation.components.InformativeMessage
import com.xorko.backlogmanager.game.presentation.gamelist.components.GameList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListScreen(
    state: GameListState,
    onAction: (GameListActions) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val gameListState = rememberLazyListState()
    var isFabVisible by remember { mutableStateOf(false) }

    // Scroll to top when search results are displayed
    LaunchedEffect(state.searchResults) {
        if (state.searchQuery.isNotBlank()) {
            snapshotFlow { gameListState.layoutInfo.totalItemsCount }.first { it > 0 }

            gameListState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(gameListState) {
        snapshotFlow { gameListState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { isFabVisible = it > 0 }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { gameListState.shouldLoadMoreItems() }
            .distinctUntilChanged()
            .collect { _ ->
                onAction(GameListActions.OnListEndAlmostReached)
            }
    }

    Scaffold(
        floatingActionButton = {
            if (isFabVisible) {
                ScrollToTopFab(
                    onClick = {
                        coroutineScope.launch {
                            gameListState.animateScrollToItem(0)
                        }
                    },
                )
            }
        },
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
    ) { innerPadding ->
        Column(
            modifier =
                Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GameSearchBar(
                hint = stringResource(Res.string.search_hint),
                searchQuery = state.searchQuery,
                onSearchQueryChange = {
                    onAction(GameListActions.OnSearchQueryChange(it))
                },
                onImeSearch = {
                    keyboardController?.hide()
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(Dimens.paddingMedium),
            )
            Spacer(modifier = Modifier.height(Dimens.spacingMedium))
            Surface(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            ) {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { onAction(GameListActions.OnRefresh) },
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                        )
                    } else {
                        when {
                            state.errorMessage != null -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    ErrorMessage(
                                        message = state.errorMessage.asString(),
                                        modifier =
                                            Modifier.padding(
                                                Dimens.paddingMedium,
                                            ),
                                    )
                                }
                            }

                            state.searchResults.isEmpty() -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    InformativeMessage(
                                        message = stringResource(Res.string.no_search_results),
                                    )
                                }
                            }

                            else -> {
                                GameList(
                                    games = state.searchResults,
                                    onGameClicked = {
                                        onAction(
                                            GameListActions.OnGameClicked(
                                                it,
                                            ),
                                        )
                                    },
                                    scrollState = gameListState,
                                    isLoadingMoreGames = state.isLoadingMoreGames,
                                    modifier = Modifier.align(Alignment.TopStart),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BacklogManagerTheme {
        GameListScreen(
            state = GameListState(
                searchResults = (0..10).map { generateGame(it) },
                searchQuery = "Game",
                isLoading = false,
            ),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun LoadingPreview() {
    BacklogManagerTheme {
        GameListScreen(
            state = GameListState(
                searchResults = emptyList(),
            ),
            onAction = {},
        )
    }
}

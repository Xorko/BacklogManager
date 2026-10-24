package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import backlogmanager.composeapp.generated.resources.backlog_search_hint
import com.xorko.backlogmanager.core.presentation.components.GameSearchBar
import com.xorko.backlogmanager.core.presentation.components.ScrollToTopFab
import com.xorko.backlogmanager.core.presentation.generateGame
import com.xorko.backlogmanager.core.presentation.randomDateTimeBetween
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.components.BackloggedGameList
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.components.BackloggedGameStatusFilter
import com.xorko.backlogmanager.game.presentation.components.ErrorMessage
import com.xorko.backlogmanager.game.presentation.components.NoSearchResults
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BackloggedGameListScreenRoot(
    viewModel: BackloggedGameListViewModel = koinViewModel(),
    onGameClick: (BackloggedGame) -> Unit,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackloggedGameListScreen(
    state: BackloggedGameListState,
    onAction: (BackloggedGameListActions) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val backloggedGamesListState = rememberLazyListState()
    var isFabVisible by remember { mutableStateOf(false) }

    // Scroll to top when search results are displayed
    LaunchedEffect(state.searchResults) {
        if (state.searchQuery.isNotBlank()) {
            snapshotFlow { backloggedGamesListState.layoutInfo.totalItemsCount }.first { it > 0 }

            backloggedGamesListState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(backloggedGamesListState) {
        snapshotFlow { backloggedGamesListState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { isFabVisible = it > 0 }
    }

    Scaffold(
        floatingActionButton = {
            if (isFabVisible) {
                ScrollToTopFab(
                    onClick = {
                        coroutineScope.launch {
                            backloggedGamesListState.animateScrollToItem(0)
                        }
                    },
                )
            }
        },
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GameSearchBar(
                hint = stringResource(Res.string.backlog_search_hint),
                searchQuery = state.searchQuery,
                onSearchQueryChange = {
                    onAction(BackloggedGameListActions.OnSearchQueryChange(it))
                },
                onImeSearch = {
                    keyboardController?.hide()
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(Dimens.paddingMedium),
            )
            BackloggedGameStatusFilter(
                selectedStatus = state.selectedFilter,
                onStatusSelected = { onAction(BackloggedGameListActions.OnFilterSelected(it)) },
                counts = state.statusCounts,
                modifier = Modifier.fillMaxWidth(),
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
                    onRefresh = { onAction(BackloggedGameListActions.OnRefresh) },
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
                                    modifier =
                                        Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    ErrorMessage(message = state.errorMessage.asString())
                                }
                            }

                            state.searchResults.isEmpty() -> {
                                Box(
                                    modifier =
                                        Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    NoSearchResults()
                                }
                            }

                            else -> {
                                BackloggedGameList(
                                    backloggedGames = state.searchResults,
                                    onBackloggedGameClick = {
                                        onAction(
                                            BackloggedGameListActions.OnBackloggedGameClicked(
                                                it,
                                            ),
                                        )
                                    },
                                    onBackloggedGameRemoved = {
                                        onAction(
                                            BackloggedGameListActions.OnRemoveBackloggedGame(
                                                it,
                                            ),
                                        )
                                    },
                                    scrollState = backloggedGamesListState,
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
private fun BackloggedGameListPreview() {
    BacklogManagerTheme {
        BackloggedGameListScreen(
            state =
                BackloggedGameListState(
                    searchResults = backloggedGameList,
                    statusCounts =
                        mapOf(
                            BackloggedGame.ToPlay::class to 1,
                            BackloggedGame.Playing::class to 2,
                            BackloggedGame.Finished::class to 3,
                        ),
                ),
        ) { }
    }
}

private val backloggedGameList =
    (1..10).map {
        val backloggedDate = randomDateTimeBetween()
        val startedDate =
            randomDateTimeBetween(
                start = backloggedDate.toInstant(TimeZone.currentSystemDefault()),
            )
        val finishedDate =
            randomDateTimeBetween(
                start = startedDate.toInstant(TimeZone.currentSystemDefault()),
            )

        when (it % 3) {
            0 -> {
                BackloggedGame.ToPlay(
                    id = it.toString(),
                    game = generateGame(it),
                    backloggedDate = randomDateTimeBetween(),
                )
            }

            1 -> {
                BackloggedGame.Playing(
                    id = it.toString(),
                    game = generateGame(it),
                    backloggedDate = backloggedDate,
                    startedDate = startedDate,
                    droppedDate = null,
                    resumedDate = null,
                )
            }

            else -> {
                BackloggedGame.Finished(
                    id = it.toString(),
                    game = generateGame(it),
                    backloggedDate = backloggedDate,
                    startedDate = startedDate,
                    finishedDate = finishedDate,
                    droppedDate = null,
                    resumedDate = null,
                )
            }
        }
    }

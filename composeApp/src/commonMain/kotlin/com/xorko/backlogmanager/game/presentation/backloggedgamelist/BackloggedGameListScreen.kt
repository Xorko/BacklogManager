package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xorko.backlogmanager.core.presentation.components.GameSearchBar
import com.xorko.backlogmanager.core.presentation.generateGame
import com.xorko.backlogmanager.core.presentation.randomDateTimeBetween
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.components.BackloggedGameFilterChips
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.components.BackloggedGameList
import com.xorko.backlogmanager.game.presentation.components.ErrorMessage
import com.xorko.backlogmanager.game.presentation.components.NoSearchResults
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
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

@Composable
fun BackloggedGameListScreen(
    state: BackloggedGameListState,
    onAction: (BackloggedGameListActions) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val backloggedGamesLazyListState = rememberLazyListState()

    LaunchedEffect(state.searchResults) {
        backloggedGamesLazyListState.animateScrollToItem(0)
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
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
            modifier = Modifier.widthIn(max = 400.dp).padding(16.dp).fillMaxWidth(),
        )
        BackloggedGameFilterChips(
            selectedStatus = state.selectedFilter,
            onStatusSelected = { onAction(BackloggedGameListActions.OnFilterSelected(it)) },
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
                            state.errorMessage != null -> {
                                ErrorMessage(message = state.errorMessage.asString())
                            }

                            state.searchResults.isEmpty() -> {
                                NoSearchResults()
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
                                    scrollState = backloggedGamesLazyListState,
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
                BackloggedGame.NowPlaying(
                    id = it.toString(),
                    game = generateGame(it),
                    backloggedDate = backloggedDate,
                    startedDate = startedDate,
                )
            }

            else -> {
                BackloggedGame.Finished(
                    id = it.toString(),
                    game = generateGame(it),
                    backloggedDate = backloggedDate,
                    startedDate = startedDate,
                    finishedDate = finishedDate,
                )
            }
        }
    }

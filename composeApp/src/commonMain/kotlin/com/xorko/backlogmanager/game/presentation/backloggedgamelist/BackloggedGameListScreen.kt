package com.xorko.backlogmanager.game.presentation.backloggedgamelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xorko.backlogmanager.core.presentation.components.GameSearchBar
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.BackloggedGame
import com.xorko.backlogmanager.game.domain.Developer
import com.xorko.backlogmanager.game.domain.Game
import com.xorko.backlogmanager.game.domain.Publisher
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.components.BackloggedGameListItem
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val game =
            Game(
                id = 1L,
                name = "Caves of Qud",
                description =
                    """
                    Caves of Qud is a science fantasy roguelike epic steeped in retrofuturism,
                    deep simulation, and swathes of sentient plants.
                    """.trimIndent(),
                released = LocalDate(year = 2015, monthNumber = 7, dayOfMonth = 15),
                coverUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/co7eda.webp",
                developers = listOf(Developer(id = 1L, name = "Freehold Games")),
                publishers = listOf(Publisher(id = 1L, name = "Kitfox Games")),
                rating = 4,
                ratingsCount = 437,
            )
        val backloggedGame =
            BackloggedGame.NowPlaying(
                backloggedDate =
                    Clock.System
                        .now()
                        .toLocalDateTime(TimeZone.currentSystemDefault()),
                startedDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                game = game,
            )

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
        Column(
            modifier =
                Modifier
                    .padding(horizontal = Dimens.paddingMedium),
            verticalArrangement = spacedBy(Dimens.spacingMedium),
        ) {
            BackloggedGameListItem(backloggedGame = backloggedGame, onClick = {})
            BackloggedGameListItem(backloggedGame = backloggedGame, onClick = {})
            BackloggedGameListItem(backloggedGame = backloggedGame, onClick = {})
        }
    }
}

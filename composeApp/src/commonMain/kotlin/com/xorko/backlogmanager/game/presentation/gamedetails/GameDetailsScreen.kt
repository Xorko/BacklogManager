package com.xorko.backlogmanager.game.presentation.gamedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.error_game_not_found
import backlogmanager.composeapp.generated.resources.game_details_screen_title
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.game.presentation.gamedetails.components.BackloggedGameTopAppBarActions
import com.xorko.backlogmanager.game.presentation.gamedetails.components.GameDetailsCarousel
import com.xorko.backlogmanager.game.presentation.gamedetails.components.GameDetailsSheet
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameDetailScreenRoot(
    viewModel: GameDetailsViewModel,
    onBackClicked: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GameDetailScreen(
        state = state,
        onAction = { action ->
            when (action) {
                GameDetailsActions.OnBack -> onBackClicked()
                else -> viewModel.onAction(action)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameDetailScreen(
    state: GameDetailsState,
    onAction: (GameDetailsActions) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.game_details_screen_title),
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(GameDetailsActions.OnBack) },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back to game list",
                        )
                    }
                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                actions = {
                    if (state.backloggedGame != null) {
                        BackloggedGameTopAppBarActions(
                            isPlaying = state.backloggedGame is BackloggedGame.Playing,
                            isDropped = state.backloggedGame is BackloggedGame.Dropped,
                            onDropGame = { onAction(GameDetailsActions.OnDrop) },
                            onResumeGame = { onAction(GameDetailsActions.OnResume) },
                            onRemoveFromBacklog = { onAction(GameDetailsActions.OnRemoveFromBacklog) },
                        )
                    }
                },
            )
        },
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                when {
                    state.errorMessage != null -> {
                        Text(
                            text = state.errorMessage.asString(),
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    state.game == null -> {
                        Text(
                            text = stringResource(Res.string.error_game_not_found),
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    else -> {
                        GameDetails(
                            game = state.game,
                            backloggedGame = state.backloggedGame,
                            onAction = onAction,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GameDetails(
    game: Game,
    backloggedGame: BackloggedGame?,
    onAction: (GameDetailsActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
    ) {
        val screenWidthPx = with(density) { maxWidth.toPx() }
        val maxCarouselHeight = remember(screenWidthPx) { screenWidthPx * 9f / 16f }
        var carouselHeight by remember { mutableStateOf(maxCarouselHeight) }
        var carouselAlpha by remember { mutableStateOf(1f) }

        val nestedScrollConnection =
            remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource,
                    ): Offset {
                        val delta = available.y.toInt()
                        val previousCarouselHeight = carouselHeight
                        carouselHeight = (carouselHeight + delta).coerceIn(0f, maxCarouselHeight)
                        val consumed = carouselHeight - previousCarouselHeight

                        carouselAlpha = (carouselHeight / maxCarouselHeight).coerceIn(0f, 1f)

                        return Offset(0f, consumed)
                    }
                }
            }

        val carouselItemWidth = maxWidth * 0.9f

        Box(
            modifier = Modifier.fillMaxSize().nestedScroll(nestedScrollConnection),
        ) {
            GameDetailsCarousel(
                game = game,
                itemWidth = carouselItemWidth,
                modifier =
                    Modifier
                        .height(with(density) { carouselHeight.toDp() })
                        .graphicsLayer { alpha = carouselAlpha }
                        .padding(top = Dimens.paddingMedium),
            )
            GameDetailsSheet(
                game = game,
                backloggedGame = backloggedGame,
                onStatusSelected = { onAction(GameDetailsActions.OnStatusSelected(it)) },
                onAddToBacklogPressed = { onAction(GameDetailsActions.OnAddToBacklog) },
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .padding(top = Dimens.paddingMedium)
                        .offset { IntOffset(0, carouselHeight.toInt()) },
            )
        }
    }
}

package com.xorko.backlogmanager.app

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import com.xorko.backlogmanager.core.data.configuration.BuildType
import com.xorko.backlogmanager.core.data.configuration.Environment
import com.xorko.backlogmanager.core.data.configuration.currentBuildType
import com.xorko.backlogmanager.core.data.configuration.currentEnvironment
import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.game.presentation.gamedetails.GameDetailScreenRoot
import com.xorko.backlogmanager.game.presentation.gamelist.GameListScreenRoot
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private const val DEFAULT_ANIMATION_DURATION = 300

@Composable
@Preview
fun App() {
    LaunchedEffect(Unit) {
        Logger.i("Current environment: $currentEnvironment", tag = Environment::class.simpleName)
        Logger.i("Current build type: $currentBuildType", tag = BuildType::class.simpleName)
    }

    setSingletonImageLoaderFactory { context ->
        ImageLoader
            .Builder(context)
            .crossfade(true)
            .build()
    }

    BacklogManagerTheme {
        val navigationState =
            rememberNavigationState(
                startRoute = Route.GameList,
                topLevelRoutes = setOf(Route.GameList),
            )
        val navigator = remember(navigationState) { Navigator(navigationState) }

        val entryProvider =
            remember(navigator) {
                entryProvider<NavKey> {
                    entry<Route.GameList> {
                        GameListScreenRoot(
                            viewModel = koinViewModel(),
                            onGameClick = { game ->
                                navigator.navigate(Route.GameDetail(game.id))
                            },
                        )
                    }
                    entry<Route.GameDetail> { key ->
                        GameDetailScreenRoot(
                            viewModel =
                                koinViewModel {
                                    parametersOf(key.gameId)
                                },
                            onBackClicked = { navigator.goBack() },
                        )
                    }
                }
            }

        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = { navigator.goBack() },
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec =
                        tween(
                            durationMillis = DEFAULT_ANIMATION_DURATION,
                            easing = FastOutSlowInEasing,
                        ),
                ) togetherWith
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                        animationSpec =
                            tween(
                                durationMillis = DEFAULT_ANIMATION_DURATION,
                                easing = FastOutSlowInEasing,
                            ),
                    )
            },
            popTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec =
                        tween(
                            durationMillis = DEFAULT_ANIMATION_DURATION,
                            easing = FastOutSlowInEasing,
                        ),
                ) togetherWith
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec =
                            tween(
                                durationMillis = DEFAULT_ANIMATION_DURATION,
                                easing = FastOutSlowInEasing,
                            ),
                    )
            },
        )
    }
}

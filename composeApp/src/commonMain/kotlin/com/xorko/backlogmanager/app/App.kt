package com.xorko.backlogmanager.app

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.xorko.backlogmanager.core.presentation.components.BottomNavigationBar
import com.xorko.backlogmanager.core.presentation.hooks.ObserveSnackbarEvents
import com.xorko.backlogmanager.core.presentation.navigation.Navigator
import com.xorko.backlogmanager.core.presentation.navigation.rememberNavigationState
import com.xorko.backlogmanager.core.presentation.navigation.toEntries
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.BackloggedGameListScreenRoot
import com.xorko.backlogmanager.game.presentation.gamedetails.GameDetailScreenRoot
import com.xorko.backlogmanager.game.presentation.gamelist.GameListScreenRoot
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private const val DEFAULT_ANIMATION_DURATION = 300

@OptIn(ExperimentalMaterial3Api::class)
@Composable
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
                startKey = Route.GameList,
                topLevelKeys = setOf(Route.GameList, Route.BackloggedGameList),
            )
        val navigator = remember(navigationState) { Navigator(navigationState) }

        val snackbarHostState = remember { SnackbarHostState() }
        ObserveSnackbarEvents(snackbarHostState = snackbarHostState)

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
                    entry<Route.BackloggedGameList> {
                        BackloggedGameListScreenRoot(
                            viewModel = koinViewModel(),
                            onGameClick = { backloggedGame ->
                                navigator.navigate(Route.GameDetail(backloggedGame.game.id))
                            },
                        )
                    }
                    entry<Route.GameDetail> { key ->
                        GameDetailScreenRoot(
                            viewModel =
                                koinViewModel {
                                    parametersOf(key.gameId)
                                },
                            onBackClicked = navigator::goBack,
                        )
                    }
                }
            }

        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    currentRoute = navigationState.currentTopLevelKey,
                    onRouteSelected = { route ->
                        navigator.navigate(route)
                    },
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            NavDisplay(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding),
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
}

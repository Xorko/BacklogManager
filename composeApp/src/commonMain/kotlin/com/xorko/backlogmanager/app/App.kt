package com.xorko.backlogmanager.app

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
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
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

private const val DEFAULT_ANIMATION_DURATION = 300
private val defaultEnterAnimation =
    slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec =
            tween(
                durationMillis = DEFAULT_ANIMATION_DURATION,
                easing = FastOutSlowInEasing,
            ),
    )
private val defaultPopExitAnimation =
    slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec =
            tween(
                durationMillis = DEFAULT_ANIMATION_DURATION,
                easing = FastOutSlowInEasing,
            ),
    )

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
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.GameGraph,
        ) {
            navigation<Route.GameGraph>(
                startDestination = Route.GameList,
            ) {
                composable<Route.GameList>(
                    enterTransition = { EnterTransition.None },
                    popExitTransition = { defaultPopExitAnimation },
                ) {
                    GameListScreenRoot(
                        viewModel = koinViewModel(),
                        onGameClick = { game ->
                            navController.navigate(Route.GameDetail(game.id))
                        },
                    )
                }
                composable<Route.GameDetail>(
                    enterTransition = { defaultEnterAnimation },
                    popExitTransition = { defaultPopExitAnimation },
                ) { entry ->
                    GameDetailScreenRoot(
                        viewModel = koinViewModel(),
                        onBackClicked = { navController.popBackStack() },
                    )
                }
            }
        }
    }
}

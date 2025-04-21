package com.xorko.backlogmanager.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import com.xorko.backlogmanager.core.data.configuration.BuildType
import com.xorko.backlogmanager.core.data.configuration.Environment
import com.xorko.backlogmanager.core.data.configuration.currentBuildType
import com.xorko.backlogmanager.core.data.configuration.currentEnvironment
import com.xorko.backlogmanager.core.domain.logging.Logger
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.game.presentation.gamelist.GameListScreenRoot
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

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
                composable<Route.GameList> {
                    GameListScreenRoot(
                        viewModel = koinViewModel(),
                        onGameClick = { game ->
                            navController.navigate(Route.GameDetail(game.id))
                        },
                    )
                }
                composable<Route.GameDetail> { entry ->
                    val args = entry.toRoute<Route.GameDetail>()

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("Game ID: ${args.gameId}")
                    }
                }
            }
        }
    }
}

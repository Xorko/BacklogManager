package com.xorko.backlogmanager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
        GameListScreenRoot(
            viewModel = koinViewModel(),
            onGameClick = {},
        )
    }
}

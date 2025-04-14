package com.xorko.backlogmanager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.BackloggedGameListScreenRoot
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.BackloggedGameListViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader
            .Builder(context)
            .crossfade(true)
            .build()
    }

    BacklogManagerTheme {
        BackloggedGameListScreenRoot(
            viewModel = remember { BackloggedGameListViewModel() },
            onGameClick = {},
        )
    }
}

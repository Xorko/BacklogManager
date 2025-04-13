package com.xorko.backlogmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.xorko.backlogmanager.core.presentation.components.GameSearchBar
import com.xorko.backlogmanager.core.presentation.theme.Background
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.BackloggedGame
import com.xorko.backlogmanager.game.domain.Developer
import com.xorko.backlogmanager.game.domain.Game
import com.xorko.backlogmanager.game.domain.Publisher
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.components.BackloggedGameListItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Preview
@Composable
private fun SearchBarPreview() {
    BacklogManagerTheme {
        Column(
            modifier = Modifier.background(Background),
            verticalArrangement = spacedBy(10.dp),
        ) {
            GameSearchBar(
                searchQuery = "",
                onSearchQueryChange = {},
                onImeSearch = {},
                modifier = Modifier.fillMaxWidth(),
            )
            GameSearchBar(
                searchQuery = "Caves of Qud",
                onSearchQueryChange = {},
                onImeSearch = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
private fun BackloggedGameListItemPreview() {
    val game =
        Game(
            id = 1L,
            name = "Caves of Qud",
            description = """
                Caves of Qud is a science fantasy roguelike epic steeped in retrofuturism,
                deep simulation, and swathes of sentient plants.
            """.trimIndent(),
            released = LocalDate(year = 2015, monthNumber = 7, dayOfMonth = 15),
            coverUrl = "https://shared.fastly.steamstatic.com/store_item_assets/steam/apps/333640/header.jpg?t=1744126759",
            developers = listOf(Developer(id = 1L, name = "Freehold Games")),
            publishers = listOf(Publisher(id = 1L, name = "Kitfox Games")),
            rating = 4,
            ratingsCount = 437,
        )
    val toPlayBackloggedGame =
        BackloggedGame.ToPlay(
            game = game,
            backloggedDate = LocalDateTime(2024, 5, 4, 0, 0, 0, 0),
        )
    val nowPlayingBackloggedGame =
        BackloggedGame.NowPlaying(
            game = game,
            backloggedDate = LocalDateTime(2024, 5, 4, 0, 0, 0, 0),
            startedDate = LocalDateTime(2024, 5, 5, 0, 0, 0, 0),
        )
    val finishedBackloggedGame =
        BackloggedGame.Finished(
            game = game,
            backloggedDate = LocalDateTime(2024, 5, 4, 0, 0, 0, 0),
            startedDate = LocalDateTime(2024, 5, 5, 0, 0, 0, 0),
            finishedDate = LocalDateTime(2024, 5, 6, 0, 0, 0, 0),
        )
    val coilPreviewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.Red.toArgb(), 300, 350)
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides coilPreviewHandler) {
        Column(verticalArrangement = spacedBy(Dimens.spacingMedium)) {
            BacklogManagerTheme {
                BackloggedGameListItem(
                    backloggedGame = toPlayBackloggedGame,
                    onClick = {},
                )
                BackloggedGameListItem(
                    backloggedGame = nowPlayingBackloggedGame,
                    onClick = {},
                )
                BackloggedGameListItem(
                    backloggedGame = finishedBackloggedGame,
                    onClick = {},
                )
            }
        }
    }
}

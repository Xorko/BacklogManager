package com.xorko.backlogmanager.game.presentation.gamelist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xorko.backlogmanager.core.presentation.conditional
import com.xorko.backlogmanager.core.presentation.generateGame
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.Game

@Composable
fun GameList(
    games: List<Game>,
    onGameClicked: (Game) -> Unit,
    isLoadingMoreGames: Boolean = false,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        state = scrollState,
        contentPadding = PaddingValues(bottom = Dimens.paddingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        itemsIndexed(
            items = games,
            key = { _, game -> game.id },
        ) { index, game ->
            GameListItem(
                game = game,
                onClick = { onGameClicked(game) },
                modifier =
                    Modifier
                        .widthIn(max = 700.dp)
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.paddingMedium)
                        .conditional(index == 0) {
                            padding(top = Dimens.paddingMedium)
                        },
            )
        }

        if (isLoadingMoreGames) {
            item {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BacklogManagerTheme {
        GameList(
            games = (0..10).map { generateGame(it) },
            onGameClicked = {},
        )
    }
}

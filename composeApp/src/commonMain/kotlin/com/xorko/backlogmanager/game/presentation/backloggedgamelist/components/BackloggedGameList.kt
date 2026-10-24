package com.xorko.backlogmanager.game.presentation.backloggedgamelist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xorko.backlogmanager.core.presentation.conditional
import com.xorko.backlogmanager.core.presentation.generateGame
import com.xorko.backlogmanager.core.presentation.randomDateTimeBetween
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Composable
fun BackloggedGameList(
    backloggedGames: List<BackloggedGame>,
    onBackloggedGameClick: (BackloggedGame) -> Unit,
    onBackloggedGameRemoved: (BackloggedGame) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = Dimens.paddingMedium),
    ) {
        itemsIndexed(
            items = backloggedGames,
            key = { _, backloggedGame -> backloggedGame.id },
        ) { index, backloggedGame ->
            BackloggedGameListItem(
                backloggedGame = backloggedGame,
                modifier =
                    Modifier
                        .widthIn(max = 700.dp)
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.paddingMedium)
                        .conditional(index == 0) {
                            padding(top = Dimens.paddingMedium)
                        },
                onClick = { onBackloggedGameClick(backloggedGame) },
                onRemove = { onBackloggedGameRemoved(backloggedGame) },
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val backloggedGameList =
        (1..10).map {
            val backloggedDate = randomDateTimeBetween()
            val startedDate =
                randomDateTimeBetween(
                    start = backloggedDate.toInstant(TimeZone.currentSystemDefault()),
                )
            val finishedDate =
                randomDateTimeBetween(
                    start = startedDate.toInstant(TimeZone.currentSystemDefault()),
                )

            when (it % 3) {
                0 -> {
                    BackloggedGame.ToPlay(
                        id = it.toString(),
                        game = generateGame(it),
                        backloggedDate = randomDateTimeBetween(),
                    )
                }

                1 -> {
                    BackloggedGame.Playing(
                        id = it.toString(),
                        game = generateGame(it),
                        backloggedDate = backloggedDate,
                        startedDate = startedDate,
                        droppedDate = null,
                        resumedDate = null,
                    )
                }

                else -> {
                    BackloggedGame.Finished(
                        id = it.toString(),
                        game = generateGame(it),
                        backloggedDate = backloggedDate,
                        startedDate = startedDate,
                        finishedDate = finishedDate,
                        droppedDate = null,
                        resumedDate = null,
                    )
                }
            }
        }

    BacklogManagerTheme {
        BackloggedGameList(
            backloggedGames = backloggedGameList,
            onBackloggedGameClick = {},
            onBackloggedGameRemoved = {},
        )
    }
}

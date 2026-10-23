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
import androidx.compose.ui.unit.dp
import com.xorko.backlogmanager.core.presentation.conditional
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.BackloggedGame

@Composable
fun BackloggedGameList(
    backloggedGames: List<BackloggedGame>,
    onBackloggedGameClick: (BackloggedGame) -> Unit,
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
            )
        }
    }
}

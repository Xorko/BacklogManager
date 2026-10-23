package com.xorko.backlogmanager.game.presentation.gamedetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.game.presentation.gamedetails.GameDetailsActions

@Composable
fun GameDetailsHeader(
    game: Game,
    backloggedGame: BackloggedGame?,
    onAction: (GameDetailsActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isInBacklog = backloggedGame != null

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = game.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.size(Dimens.spacingSmall))
        Text(
            text = "${
                game.genres.joinToString(", ") { it.name }
            } • ${game.platforms.firstOrNull()?.name ?: ""} • ${game.initialReleaseDate?.year ?: ""}",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.size(Dimens.spacingMedium))
        if (isInBacklog) {
            BacklogStatusSelector(
                selectedStatus = backloggedGame::class,
                onStatusSelected = { }, // TODO: Add action
            )
        }
        Spacer(Modifier.size(Dimens.spacingMedium))
        if (isInBacklog) {
            RemoveFromBacklogButton(onClick = { onAction(GameDetailsActions.OnRemoveFromBacklog) })
        } else {
            AddToBacklogButton(onClick = { onAction(GameDetailsActions.OnAddToBacklog) })
        }
    }
}

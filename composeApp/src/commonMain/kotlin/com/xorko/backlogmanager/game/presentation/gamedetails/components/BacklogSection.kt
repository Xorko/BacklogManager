package com.xorko.backlogmanager.game.presentation.gamedetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.backlogged_game_added_on
import backlogmanager.composeapp.generated.resources.backlogged_game_finished_on
import backlogmanager.composeapp.generated.resources.backlogged_game_started_on
import com.xorko.backlogmanager.core.presentation.generateGame
import com.xorko.backlogmanager.core.presentation.randomDateTimeBetween
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.presentation.gamedetails.GameDetailsActions
import com.xorko.backlogmanager.utils.toMediumDateFormat
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.jetbrains.compose.resources.stringResource

@Composable
fun BacklogSection(
    backloggedGame: BackloggedGame?,
    onAction: (GameDetailsActions) -> Unit,
) {
    val isInBacklog = backloggedGame != null

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isInBacklog) {
            BacklogStatusSelector(
                selectedStatus = backloggedGame::class,
                onStatusSelected = { onAction(GameDetailsActions.OnStatusSelected(it)) },
            )
            Spacer(Modifier.size(Dimens.spacingMedium))
            Dates(backloggedGame = backloggedGame)
        }
        Spacer(Modifier.size(Dimens.spacingMedium))
        AddOrRemoveFromBacklogButton(
            isInBacklog = isInBacklog,
            onAction = onAction,
        )
    }
}

@Composable
private fun Dates(
    backloggedGame: BackloggedGame,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (backloggedGame is BackloggedGame.ToPlay || backloggedGame is BackloggedGame.Playing ||
            backloggedGame is BackloggedGame.Finished
        ) {
            DateRow(
                icon = Icons.Outlined.Add,
                label = stringResource(Res.string.backlogged_game_added_on),
                date = backloggedGame.backloggedDate.toMediumDateFormat(),
            )
        }
        if (backloggedGame is BackloggedGame.Playing || backloggedGame is BackloggedGame.Finished) {
            DateRow(
                icon = Icons.Outlined.PlayArrow,
                label = stringResource(Res.string.backlogged_game_started_on),
                date = backloggedGame.startedDate.toMediumDateFormat(),
            )
        }
        if (backloggedGame is BackloggedGame.Finished) {
            DateRow(
                icon = Icons.Outlined.Check,
                label = stringResource(Res.string.backlogged_game_finished_on),
                date = backloggedGame.finishedDate.toMediumDateFormat(),
            )
        }
    }
}

@Composable
private fun DateRow(
    icon: ImageVector,
    label: String,
    date: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSmall),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(Dimens.iconSmall),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = date,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun AddOrRemoveFromBacklogButton(
    isInBacklog: Boolean,
    onAction: (GameDetailsActions) -> Unit,
) {
    if (isInBacklog) {
        RemoveFromBacklogButton(onClick = { onAction(GameDetailsActions.OnRemoveFromBacklog) })
    } else {
        AddToBacklogButton(onClick = { onAction(GameDetailsActions.OnAddToBacklog) })
    }
}

@Preview
@Composable
private fun notInBacklogPreview() {
    BacklogManagerTheme {
        BacklogSection(
            backloggedGame = null,
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun inBacklogPreview() {
    val backloggedDate = randomDateTimeBetween()
    val startedDate =
        randomDateTimeBetween(
            start = backloggedDate.toInstant(TimeZone.currentSystemDefault()),
        )
    val finishedDate =
        randomDateTimeBetween(
            start = startedDate.toInstant(TimeZone.currentSystemDefault()),
        )

    BacklogManagerTheme {
        BacklogSection(
            backloggedGame =
                BackloggedGame.Finished(
                    id = "1",
                    game = generateGame(1),
                    backloggedDate = backloggedDate,
                    startedDate = startedDate,
                    finishedDate = finishedDate,
                ),
            onAction = {},
        )
    }
}

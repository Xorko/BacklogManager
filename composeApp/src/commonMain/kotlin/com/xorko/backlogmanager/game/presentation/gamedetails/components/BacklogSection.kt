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
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.backlogged_game_added_on
import backlogmanager.composeapp.generated.resources.backlogged_game_drop
import backlogmanager.composeapp.generated.resources.backlogged_game_dropped_on
import backlogmanager.composeapp.generated.resources.backlogged_game_finished_on
import backlogmanager.composeapp.generated.resources.backlogged_game_resumed_on
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
import kotlin.reflect.KClass

@Composable
fun BacklogSection(
    backloggedGame: BackloggedGame?,
    onStatusSelected: (KClass<out BackloggedGame>) -> Unit,
    onAddToBacklogPressed: () -> Unit,
) {
    val isInBacklog = backloggedGame != null

    if (isInBacklog) {
        InBacklogBacklogSection(
            backloggedGame = backloggedGame,
            onStatusSelected = onStatusSelected,
        )
    } else {
        NotInBacklogBacklogSection(
            onAddToBacklogPressed = onAddToBacklogPressed,
        )
    }
}

@Composable
private fun NotInBacklogBacklogSection(onAddToBacklogPressed: () -> Unit) {
    AddToBacklogButton(onClick = onAddToBacklogPressed)
}

@Composable
private fun InBacklogBacklogSection(
    backloggedGame: BackloggedGame,
    onStatusSelected: (KClass<out BackloggedGame>) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BacklogStatusSelector(
            selectedStatus = backloggedGame::class,
            onStatusSelected = onStatusSelected,
        )
        Spacer(Modifier.size(Dimens.spacingMedium))
        Dates(backloggedGame = backloggedGame)
    }
}

@Composable
private fun Dates(
    backloggedGame: BackloggedGame,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        DateRow(
            icon = Icons.Outlined.Add,
            label = stringResource(Res.string.backlogged_game_added_on),
            date = backloggedGame.backloggedDate.toMediumDateFormat(),
        )
        if (backloggedGame is BackloggedGame.Started) {
            DateRow(
                icon = Icons.Outlined.PlayArrow,
                label = stringResource(Res.string.backlogged_game_started_on),
                date = backloggedGame.startedDate.toMediumDateFormat(),
            )

            val droppedDate =
                when (backloggedGame) {
                    is BackloggedGame.Droppable -> backloggedGame.droppedDate
                    is BackloggedGame.Dropped -> backloggedGame.droppedDate
                }
            val resumedDate = (backloggedGame as? BackloggedGame.Droppable)?.resumedDate

            droppedDate?.let {
                DateRow(
                    icon = Icons.Outlined.Close,
                    label = stringResource(Res.string.backlogged_game_dropped_on),
                    date = it.toMediumDateFormat(),
                )
            }
            resumedDate?.let {
                DateRow(
                    icon = Icons.Outlined.Refresh,
                    label = stringResource(Res.string.backlogged_game_resumed_on),
                    date = it.toMediumDateFormat(),
                )
            }
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

@Preview
@Composable
private fun notInBacklogPreview() {
    BacklogManagerTheme {
        BacklogSection(
            backloggedGame = null,
            onStatusSelected = {},
            onAddToBacklogPressed = {},
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
    val backloggedGame =
        BackloggedGame.Finished(
            id = "1",
            game = generateGame(1),
            backloggedDate = backloggedDate,
            startedDate = startedDate,
            droppedDate = startedDate,
            resumedDate = startedDate,
            finishedDate = finishedDate,
        )

    BacklogManagerTheme {
        BacklogSection(
            backloggedGame = backloggedGame,
            onStatusSelected = {},
            onAddToBacklogPressed = {},
        )
    }
}

@Preview
@Composable
private fun inBacklogDroppedPreview() {
    val backloggedDate = randomDateTimeBetween()
    val startedDate =
        randomDateTimeBetween(
            start = backloggedDate.toInstant(TimeZone.currentSystemDefault()),
        )
    val backloggedGame =
        BackloggedGame.Dropped(
            id = "1",
            game = generateGame(1),
            backloggedDate = backloggedDate,
            startedDate = startedDate,
            droppedDate = startedDate,
        )

    BacklogManagerTheme {
        BacklogSection(
            backloggedGame = backloggedGame,
            onStatusSelected = {},
            onAddToBacklogPressed = {},
        )
    }
}

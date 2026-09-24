package com.xorko.backlogmanager.game.presentation.gamedetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.add_to_backlog
import backlogmanager.composeapp.generated.resources.remove_from_backlog
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.game.presentation.gamedetails.GameDetailsActions
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameDetailsHeader(
    game: Game,
    isInBacklog: Boolean,
    onAction: (GameDetailsActions) -> Unit,
    modifier: Modifier = Modifier,
) {
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
        Button(
            onClick = { onAction(GameDetailsActions.OnAddToBacklog) },
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
        ) {
            Text(
                text =
                    if (isInBacklog) {
                        stringResource(Res.string.remove_from_backlog)
                    } else {
                        stringResource(Res.string.add_to_backlog)
                    },
            )
        }
    }
}

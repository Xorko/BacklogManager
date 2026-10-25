package com.xorko.backlogmanager.game.presentation.gamedetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastRoundToInt
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.critics_rating
import backlogmanager.composeapp.generated.resources.developers
import backlogmanager.composeapp.generated.resources.publishers
import backlogmanager.composeapp.generated.resources.release_date
import backlogmanager.composeapp.generated.resources.reviews
import backlogmanager.composeapp.generated.resources.users_rating
import com.xorko.backlogmanager.core.presentation.generateGame
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.utils.toLongDateFormat
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = label,
            Modifier.width(96.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun GameDetailsSection(
    game: Game,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (game.developers.isNotEmpty()) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailRow(
                label = pluralStringResource(Res.plurals.developers, game.developers.size),
                value = game.developers.joinToString(", ") { it.name },
            )
        }
        if (game.publishers.isNotEmpty()) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailRow(
                label = pluralStringResource(Res.plurals.publishers, game.publishers.size),
                value = game.publishers.joinToString(", ") { it.name },
            )
        }
        game.initialReleaseDate?.let {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailRow(
                label = stringResource(Res.string.release_date),
                value = it.toLongDateFormat(),
            )
        }
        game.criticsRating?.let {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailRow(
                label = stringResource(Res.string.critics_rating),
                value = "${it.value.fastRoundToInt()}/100 (${it.count} ${
                    pluralStringResource(
                        Res.plurals.reviews,
                        it.count,
                    ).lowercase()
                })",
            )
        }
        game.usersRating?.let {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DetailRow(
                label = stringResource(Res.string.users_rating),
                value = "${it.value.fastRoundToInt()}/100 (${it.count} ${
                    pluralStringResource(
                        Res.plurals.reviews,
                        it.count,
                    ).lowercase()
                })",
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Preview
@Composable
private fun Preview() {
    BacklogManagerTheme {
        GameDetailsSection(game = generateGame(1))
    }
}

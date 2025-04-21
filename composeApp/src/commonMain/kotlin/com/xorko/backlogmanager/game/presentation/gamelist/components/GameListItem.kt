package com.xorko.backlogmanager.game.presentation.gamelist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerShapes
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.game.presentation.components.ArtworkImage
import com.xorko.backlogmanager.game.presentation.components.Companies
import com.xorko.backlogmanager.game.presentation.components.GameTitle
import com.xorko.backlogmanager.utils.toLongDateFormat

@Composable
fun GameListItem(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = BacklogManagerShapes.medium,
        elevation = CardDefaults.cardElevation(Dimens.elevationSmall),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .padding(Dimens.paddingMedium)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GameInfoWithArtwork(
                game = game,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(Dimens.spacingMedium))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
private fun GameInfoWithArtwork(
    game: Game,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium),
    ) {
        ArtworkImage(game = game)
        GameInfo(game = game)
    }
}

@Composable
private fun GameInfo(
    game: Game,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingSmall),
        ) {
            GameTitle(game = game)
            Companies(companies = game.developers, modifier = Modifier)
            Companies(companies = game.publishers, modifier = Modifier)
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = game.initialReleaseDate?.toLongDateFormat() ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )
    }
}

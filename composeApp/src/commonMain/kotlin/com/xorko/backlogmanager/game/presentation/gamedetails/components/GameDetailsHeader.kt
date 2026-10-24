package com.xorko.backlogmanager.game.presentation.gamedetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xorko.backlogmanager.core.presentation.generateGame
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.game.presentation.components.PlatformTags
import com.xorko.backlogmanager.game.presentation.components.toPlatformUi

@Composable
fun GameDetailsHeader(
    game: Game,
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
            text = "${game.developers.joinToString(", ") { it.name }} · ${game.initialReleaseDate?.year}",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.size(Dimens.spacingSmall))
        Text(
            text = game.genres.joinToString(", ") { it.name },
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.size(Dimens.spacingMedium))
        PlatformTags(
            platforms = remember(game.platforms) { game.platforms.toPlatformUi() },
        )
    }
}

@Preview
@Composable
private fun Preview() {
    BacklogManagerTheme {
        GameDetailsHeader(
            game = generateGame(1),
        )
    }
}

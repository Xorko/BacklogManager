package com.xorko.backlogmanager.game.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.xorko.backlogmanager.game.domain.models.Game
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.formatDevelopersAndPublishers

@Composable
fun GameTitle(
    game: Game,
    modifier: Modifier = Modifier,
) {
    Text(
        text = game.name,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}

@Composable
fun GameDevelopersAndPublishers(
    game: Game,
    modifier: Modifier = Modifier,
) {
    Text(
        text = game.formatDevelopersAndPublishers(),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
        modifier = modifier,
    )
}

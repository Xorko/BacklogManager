package com.xorko.backlogmanager.game.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.xorko.backlogmanager.game.domain.models.Company
import com.xorko.backlogmanager.game.domain.models.Game

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
fun Companies(
    companies: List<Company>,
    modifier: Modifier = Modifier,
) {
    Text(
        text = companies.joinToString { it.name },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
        modifier = modifier,
    )
}

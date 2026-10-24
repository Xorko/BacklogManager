package com.xorko.backlogmanager.game.presentation.backloggedgamelist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xorko.backlogmanager.core.presentation.generateGame
import com.xorko.backlogmanager.core.presentation.randomDateTimeBetween
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerShapes
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.core.presentation.theme.PrimaryLight
import com.xorko.backlogmanager.core.presentation.theme.Success
import com.xorko.backlogmanager.game.domain.models.BackloggedGame
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.getStatus
import com.xorko.backlogmanager.game.presentation.components.ArtworkImage
import com.xorko.backlogmanager.game.presentation.components.Companies
import com.xorko.backlogmanager.game.presentation.components.PlatformTags
import com.xorko.backlogmanager.game.presentation.components.toPlatformUi
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackloggedGameListItem(
    backloggedGame: BackloggedGame,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onRemove: () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState()

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = { SwipeToDismissBox() },
        onDismiss = { dismissValue: SwipeToDismissBoxValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                onRemove()
            }
        },
        modifier = modifier,
    ) {
        BackloggedGameCard(
            backloggedGame = backloggedGame,
            onClick = onClick,
        )
    }
}

@Composable
private fun BackloggedGameCard(
    backloggedGame: BackloggedGame,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
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
                backloggedGame = backloggedGame,
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
    backloggedGame: BackloggedGame,
    modifier: Modifier = Modifier,
) {
    // Grow with the system font size so the text never gets clipped
    val fontScale = LocalDensity.current.fontScale.coerceIn(1f, 1.5f)

    Row(
        modifier = modifier.height(128.dp * fontScale),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium),
    ) {
        ArtworkImage(
            game = backloggedGame.game,
            modifier = Modifier.fillMaxHeight(),
        )
        GameInfo(
            backloggedGame = backloggedGame,
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
        )
    }
}

@Composable
private fun GameInfo(
    backloggedGame: BackloggedGame,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmall),
    ) {
        Text(
            text = backloggedGame.game.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Companies(companies = backloggedGame.game.developers, modifier = Modifier)
        PlatformTags(
            platforms =
                remember(backloggedGame.game.platforms) {
                    backloggedGame.game.platforms.toPlatformUi()
                },
            collapsedCount = 3,
            maxLines = 1,
            expandable = false,
            horizontalAlignment = Alignment.Start,
        )
        Spacer(Modifier.weight(1f))
        StatusTag(
            status = backloggedGame.getStatus().asString(),
            backgroundColor = backloggedGame.getChipColor(),
        )
    }
}

@Composable
fun StatusTag(
    status: String,
    backgroundColor: Color,
    contentColor: Color = Color.White,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = BacklogManagerShapes.small,
        modifier = modifier,
    ) {
        Text(
            text = status,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = Dimens.paddingSmall, vertical = 4.dp),
        )
    }
}

@Composable
private fun BackloggedGame.getChipColor(): Color =
    when (this) {
        is BackloggedGame.ToPlay -> PrimaryLight
        is BackloggedGame.Playing -> MaterialTheme.colorScheme.primary
        is BackloggedGame.Finished -> Success
        is BackloggedGame.Dropped -> MaterialTheme.colorScheme.error
    }

@Composable
private fun SwipeToDismissBox() {
    val color = MaterialTheme.colorScheme.errorContainer
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color, shape = BacklogManagerShapes.medium)
                .padding(horizontal = Dimens.paddingMedium),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onErrorContainer,
        )
    }
}

@Preview
@Composable
private fun BackloggedGameListItemPreview() {
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
        BackloggedGameListItem(
            backloggedGame =
                BackloggedGame.Finished(
                    id = "1",
                    game = generateGame(1),
                    backloggedDate = backloggedDate,
                    startedDate = startedDate,
                    droppedDate = null,
                    resumedDate = null,
                    finishedDate = finishedDate,
                ),
            onClick = {},
            onRemove = {},
        )
    }
}

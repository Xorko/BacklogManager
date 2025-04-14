package com.xorko.backlogmanager.game.presentation.backloggedgamelist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.game_cover_artwork_description
import backlogmanager.composeapp.generated.resources.ic_broken_image
import coil3.compose.rememberAsyncImagePainter
import com.xorko.backlogmanager.core.presentation.shimmerEffect
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerShapes
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.core.presentation.theme.PrimaryLight
import com.xorko.backlogmanager.core.presentation.theme.Success
import com.xorko.backlogmanager.game.domain.BackloggedGame
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.formatDevelopersAndPublishers
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.getLongStatus
import com.xorko.backlogmanager.game.presentation.backloggedgamelist.getStatus
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BackloggedGameListItem(
    backloggedGame: BackloggedGame,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = BacklogManagerShapes.medium,
        elevation = CardDefaults.cardElevation(Dimens.elevationSmall),
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
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium),
    ) {
        ArtworkImage(backloggedGame = backloggedGame)
        GameInfo(
            backloggedGame = backloggedGame,
        )
    }
}

@Composable
private fun GameInfo(
    backloggedGame: BackloggedGame,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingSmall),
        ) {
            Text(
                text = backloggedGame.game.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = backloggedGame.formatDevelopersAndPublishers(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            )
            StatusChip(
                status = backloggedGame.getStatus().asString(),
                backgroundColor = backloggedGame.getChipColor(),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = backloggedGame.getLongStatus().asString(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )
    }
}

@Composable
private fun ArtworkImage(
    backloggedGame: BackloggedGame,
    modifier: Modifier = Modifier,
) {
    var imageLoadResult by remember { mutableStateOf<Result<Painter>?>(null) }
    val painter =
        rememberAsyncImagePainter(
            model = backloggedGame.game.coverUrl,
            onSuccess = { imageLoadResult = Result.success(it.painter) },
            onError = {
                it.result.throwable.printStackTrace()
                imageLoadResult = Result.failure(it.result.throwable)
            },
        )

    Box(
        modifier =
            Modifier
                .width(80.dp)
                .aspectRatio(ratio = 0.65f, matchHeightConstraintsFirst = true)
                .clip(BacklogManagerShapes.small),
    ) {
        when (val result = imageLoadResult) {
            null ->
                Box(
                    modifier =
                        modifier
                            .fillMaxSize()
                            .shimmerEffect(),
                )

            else ->
                Image(
                    painter = if (result.isSuccess) painter else painterResource(Res.drawable.ic_broken_image),
                    contentDescription =
                        stringResource(
                            Res.string.game_cover_artwork_description,
                            backloggedGame.game.name,
                        ),
                    modifier = modifier.fillMaxSize(),
                    contentScale = if (result.isSuccess) ContentScale.Crop else ContentScale.Fit,
                )
        }
    }
}

@Composable
fun StatusChip(
    status: String,
    backgroundColor: Color,
    contentColor: Color = Color.White,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = BacklogManagerShapes.medium,
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
        is BackloggedGame.NowPlaying -> MaterialTheme.colorScheme.primary
        is BackloggedGame.Finished -> Success
    }

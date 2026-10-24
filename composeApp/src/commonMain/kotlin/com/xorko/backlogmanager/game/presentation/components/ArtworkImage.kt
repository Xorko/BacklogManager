package com.xorko.backlogmanager.game.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.game_cover_artwork_description
import backlogmanager.composeapp.generated.resources.ic_broken_image
import coil3.compose.rememberAsyncImagePainter
import com.xorko.backlogmanager.core.presentation.shimmerEffect
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerShapes
import com.xorko.backlogmanager.game.domain.models.Game
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArtworkImage(
    game: Game,
    modifier: Modifier = Modifier,
) {
    var imageLoadResult by remember { mutableStateOf<Result<Painter>?>(null) }
    val painter =
        rememberAsyncImagePainter(
            model = game.coverUrl,
            onSuccess = { imageLoadResult = Result.success(it.painter) },
            onError = {
                it.result.throwable.printStackTrace()
                imageLoadResult = Result.failure(it.result.throwable)
            },
        )

    Box(
        modifier =
            Modifier
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
                            game.name,
                        ),
                    modifier = modifier.fillMaxSize(),
                    contentScale = if (result.isSuccess) ContentScale.Crop else ContentScale.Fit,
                )
        }
    }
}

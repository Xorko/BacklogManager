package com.xorko.backlogmanager.game.presentation.gamedetails.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.game_cover_artwork_description
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.Game
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsCarousel(
    game: Game,
    itemWidth: Dp,
    modifier: Modifier = Modifier,
) {
    val context = LocalPlatformContext.current

    val carouselState = rememberCarouselState { game.artworksUrls.size + 1 }
    val images = remember(game) { listOfNotNull(game.coverUrl, *game.artworksUrls.toTypedArray()) }

    val imageWidthPx = with(LocalDensity.current) { itemWidth.toPx() }.roundToInt()
    val imageHeightPx =
        remember(imageWidthPx) { (imageWidthPx * 9f / 16f).roundToInt() }

    HorizontalUncontainedCarousel(
        state = carouselState,
        itemWidth = itemWidth,
        itemSpacing = Dimens.spacingMedium,
        contentPadding = PaddingValues(horizontal = Dimens.paddingMedium),
        modifier = modifier.fillMaxWidth(),
    ) { index ->
        AsyncImage(
            model =
                ImageRequest
                    .Builder(context)
                    .data(images[index])
                    .size(imageWidthPx, imageHeightPx)
                    .build(),
            contentDescription = stringResource(Res.string.game_cover_artwork_description, game.name),
            modifier = Modifier.fillMaxSize().maskClip(MaterialTheme.shapes.extraLarge),
            contentScale =
                if (game.coverUrl != null && index == 0) {
                    ContentScale.Fit
                } else {
                    ContentScale.FillBounds
                }, // Don't enlarge the cover otherwise it will look bad
        )
    }
}

package com.xorko.backlogmanager.game.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.less
import com.xorko.backlogmanager.core.presentation.conditional
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerShapes
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import com.xorko.backlogmanager.game.domain.models.Platform
import org.jetbrains.compose.resources.stringResource

enum class PlatformFamily { PlayStation, Xbox, Nintendo, PC, Other }

data class PlatformUi(
    val label: String,
    val family: PlatformFamily,
)

private fun String.toPlatformUi(): PlatformUi =
    when {
        startsWith("PlayStation", ignoreCase = true) -> {
            PlatformUi(replace("PlayStation ", "PS"), PlatformFamily.PlayStation)
        }

        startsWith("Xbox", ignoreCase = true) -> {
            PlatformUi(replace("|", "/"), PlatformFamily.Xbox)
        }

        startsWith("Nintendo", ignoreCase = true) -> {
            PlatformUi(removePrefix("Nintendo "), PlatformFamily.Nintendo)
        }

        startsWith("PC", ignoreCase = true) -> {
            PlatformUi("PC", PlatformFamily.PC)
        }

        else -> {
            PlatformUi(this, PlatformFamily.Other)
        }
    }

fun Platform.toPlatformUi(): PlatformUi = name.toPlatformUi()

fun List<Platform>.toPlatformUi(): List<PlatformUi> =
    map { it.toPlatformUi() }
        .distinct()
        .sortedWith(compareBy({ it.family.ordinal }, { it.label }))

@Composable
private fun PlatformFamily.containerColor(): Color {
    val base = MaterialTheme.colorScheme.surfaceVariant
    val accent =
        when (this) {
            PlatformFamily.PlayStation -> Color(0xFF3B6FD4)
            PlatformFamily.Xbox -> Color(0xFF2E9E4F)
            PlatformFamily.Nintendo -> Color(0xFFD9534F)
            PlatformFamily.PC -> Color(0xFF8E8E9A)
            PlatformFamily.Other -> return base.copy(alpha = 0.5f)
        }

    return accent.copy(alpha = 0.25f).compositeOver(base)
}

@Composable
private fun PlatformTag(
    platform: PlatformUi,
    modifier: Modifier = Modifier,
) {
    val container = platform.family.containerColor()
    Surface(
        modifier = modifier,
        shape = BacklogManagerShapes.small,
        color = container,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Text(
            text = platform.label,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlatformTags(
    platforms: List<PlatformUi>,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    collapsedCount: Int = 6,
    maxLines: Int = Int.MAX_VALUE,
    expandable: Boolean = true,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val canExpand = platforms.size > collapsedCount
    val visible = if (expanded || !canExpand) platforms else platforms.take(collapsedCount)
    val hidden = platforms.size - visible.size

    FlowRow(
        modifier =
            modifier
                .fillMaxWidth()
                .animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.marginSmall, horizontalAlignment),
        verticalArrangement = Arrangement.spacedBy(Dimens.marginSmall),
        maxLines = maxLines,
    ) {
        visible.forEach { PlatformTag(it) }

        if (canExpand) {
            PlatformTag(
                platform =
                    PlatformUi(
                        label = if (expanded) stringResource(Res.string.less) else "+$hidden",
                        family = PlatformFamily.Other,
                    ),
                modifier =
                    Modifier.conditional(expandable) {
                        clickable {
                            expanded = !expanded
                        }
                    },
            )
        }
    }
}

@Preview
@Composable
private fun PlatformTagsPreview() {
    BacklogManagerTheme {
        PlatformTags(
            platforms =
                listOf(
                    PlatformUi("PS5", PlatformFamily.PlayStation),
                    PlatformUi("Xbox Series X", PlatformFamily.Xbox),
                    PlatformUi("Nintendo Switch", PlatformFamily.Nintendo),
                    PlatformUi("PC", PlatformFamily.PC),
                    PlatformUi("PS4", PlatformFamily.PlayStation),
                    PlatformUi("Xbox One", PlatformFamily.Xbox),
                    PlatformUi("Nintendo 3DS", PlatformFamily.Nintendo),
                    PlatformUi("PS3", PlatformFamily.PlayStation),
                ),
        )
    }
}

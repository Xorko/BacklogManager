package com.xorko.backlogmanager.core.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.scroll_to_top
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScrollToTopFab(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = stringResource(Res.string.scroll_to_top),
        )
    }
}

package com.xorko.backlogmanager.game.presentation.gamedetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.backlogged_game_drop
import backlogmanager.composeapp.generated.resources.backlogged_game_resume
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun DropButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall),
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
            )
            Text(
                text = stringResource(Res.string.backlogged_game_drop),
            )
        }
    }
}

@Composable
fun ResumeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall),
        ) {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = null,
            )
            Text(
                text = stringResource(Res.string.backlogged_game_resume),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BacklogManagerTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingSmall),
        ) {
            DropButton {}
            ResumeButton {}
        }
    }
}

package com.xorko.backlogmanager.game.presentation.gamedetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.remove_from_backlog
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun RemoveFromBacklogButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier =
            modifier
                .padding(horizontal = 16.dp)
                .minimumInteractiveComponentSize(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall),
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )
            Text(
                text = stringResource(Res.string.remove_from_backlog),
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Preview
@Composable
private fun RemoveFromBacklogButtonPreview() {
    BacklogManagerTheme {
        RemoveFromBacklogButton {}
    }
}

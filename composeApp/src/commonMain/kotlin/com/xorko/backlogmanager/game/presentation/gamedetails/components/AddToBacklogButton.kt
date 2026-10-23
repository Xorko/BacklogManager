package com.xorko.backlogmanager.game.presentation.gamedetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.add_to_backlog
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddToBacklogButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier =
            modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.paddingSmall),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
            )
            Text(
                text = stringResource(Res.string.add_to_backlog),
            )
        }
    }
}

@Preview
@Composable
private fun AddToBacklogButtonPreview() {
    BacklogManagerTheme {
        AddToBacklogButton {}
    }
}

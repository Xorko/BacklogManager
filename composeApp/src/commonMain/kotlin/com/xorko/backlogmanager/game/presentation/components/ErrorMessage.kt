package com.xorko.backlogmanager.game.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme

@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = message,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun Preview() {
    BacklogManagerTheme {
        ErrorMessage(message = "Something went wrong")
    }
}

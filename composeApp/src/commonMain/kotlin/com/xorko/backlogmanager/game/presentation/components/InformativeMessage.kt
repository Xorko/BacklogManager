package com.xorko.backlogmanager.game.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.no_search_results
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun InformativeMessage(
    message: String,
) {
    Box(
        modifier =
            Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    BacklogManagerTheme {
        InformativeMessage(
            message = stringResource(Res.string.no_search_results),
        )
    }
}
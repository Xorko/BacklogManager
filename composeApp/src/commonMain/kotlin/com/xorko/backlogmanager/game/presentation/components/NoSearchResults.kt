package com.xorko.backlogmanager.game.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.no_search_results
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoSearchResults() {
    Text(
        text = stringResource(Res.string.no_search_results),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall,
    )
}

@Preview
@Composable
private fun Preview() {
    BacklogManagerTheme {
        NoSearchResults()
    }
}
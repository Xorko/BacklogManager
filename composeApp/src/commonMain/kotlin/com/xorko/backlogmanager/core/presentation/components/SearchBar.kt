package com.xorko.backlogmanager.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.clear_search
import backlogmanager.composeapp.generated.resources.search_hint
import com.xorko.backlogmanager.core.presentation.theme.Background
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerShapes
import com.xorko.backlogmanager.core.presentation.theme.BacklogManagerTheme
import com.xorko.backlogmanager.core.presentation.theme.Primary
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onImeSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalTextSelectionColors provides
            TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.primary,
            ),
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            shape = BacklogManagerShapes.medium,
            colors =
                OutlinedTextFieldDefaults.colors(
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = Primary,
                ),
            placeholder = {
                Text(
                    text = stringResource(Res.string.search_hint),
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(Res.string.clear_search),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            },
            trailingIcon = {
                AnimatedVisibility(
                    visible = searchQuery.isNotBlank(),
                    enter = scaleIn(),
                    exit = scaleOut(),
                ) {
                    IconButton(
                        onClick = {
                            onSearchQueryChange("")
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            },
            singleLine = true,
            keyboardActions =
                KeyboardActions(
                    onSearch = { onImeSearch() },
                ),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                ),
            modifier =
                modifier
                    .background(
                        shape = BacklogManagerShapes.medium,
                        color = MaterialTheme.colorScheme.surface,
                    ).minimumInteractiveComponentSize(),
        )
    }
}

@Preview
@Composable
private fun SearchBarPreview() {
    BacklogManagerTheme {
        Column(
            modifier = Modifier.background(Background),
            verticalArrangement = spacedBy(10.dp),
        ) {
            GameSearchBar(
                searchQuery = "",
                onSearchQueryChange = {},
                onImeSearch = {},
                modifier = Modifier.fillMaxWidth(),
            )
            GameSearchBar(
                searchQuery = "Caves of Qud",
                onSearchQueryChange = {},
                onImeSearch = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

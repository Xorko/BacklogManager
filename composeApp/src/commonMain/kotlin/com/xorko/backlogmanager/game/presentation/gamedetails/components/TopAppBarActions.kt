package com.xorko.backlogmanager.game.presentation.gamedetails.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.backlogged_game_drop
import backlogmanager.composeapp.generated.resources.backlogged_game_resume
import backlogmanager.composeapp.generated.resources.more_actions
import backlogmanager.composeapp.generated.resources.remove_from_backlog
import org.jetbrains.compose.resources.stringResource

@Composable
fun BackloggedGameTopAppBarActions(
    isPlaying: Boolean,
    isDropped: Boolean,
    onDropGame: () -> Unit,
    onResumeGame: () -> Unit,
    onRemoveFromBacklog: () -> Unit,
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { isMenuOpen = true }) {
            Icon(Icons.Outlined.MoreVert, contentDescription = stringResource(Res.string.more_actions))
        }
        DropdownMenu(expanded = isMenuOpen, onDismissRequest = { isMenuOpen = false }) {
            when {
                isPlaying -> {
                    DropMenuItem(
                        onClick = {
                            isMenuOpen = false
                            onDropGame()
                        },
                    )
                }

                isDropped -> {
                    ResumeMenuItem(
                        onClick = {
                            isMenuOpen = false
                            onResumeGame()
                        },
                    )
                }
            }
            if (isPlaying || isDropped) {
                HorizontalDivider()
            }
            RemoveFromBacklogMenuItem(
                onClick = {
                    isMenuOpen = false
                    onRemoveFromBacklog()
                },
            )
        }
    }
}

@Composable
private fun DropMenuItem(onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(stringResource(Res.string.backlogged_game_drop)) },
        leadingIcon = {
            Icon(
                Icons.Outlined.Close,
                contentDescription = null,
            )
        },
        onClick = onClick,
    )
}

@Composable
private fun ResumeMenuItem(onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(stringResource(Res.string.backlogged_game_resume)) },
        leadingIcon = {
            Icon(
                Icons.Outlined.Refresh,
                contentDescription = null,
            )
        },
        onClick = onClick,
    )
}

@Composable
private fun RemoveFromBacklogMenuItem(onClick: () -> Unit) {
    DropdownMenuItem(
        text = {
            Text(
                text = stringResource(Res.string.remove_from_backlog),
                color = MaterialTheme.colorScheme.error,
            )
        },
        leadingIcon = {
            Icon(
                Icons.Outlined.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )
        },
        onClick = onClick,
    )
}

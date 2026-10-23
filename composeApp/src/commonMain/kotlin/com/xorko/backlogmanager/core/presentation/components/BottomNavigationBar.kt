package com.xorko.backlogmanager.core.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import backlogmanager.composeapp.generated.resources.Res
import backlogmanager.composeapp.generated.resources.navigation_backlog
import backlogmanager.composeapp.generated.resources.navigation_games
import com.xorko.backlogmanager.app.Route
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomNavigationBar(
    currentRoute: NavKey,
    onRouteSelected: (Route) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
    ) {
        NavigationBarItem(
            selected = currentRoute == Route.GameList,
            onClick = { onRouteSelected(Route.GameList) },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = stringResource(Res.string.navigation_games),
                )
            },
            label = {
                Text(text = stringResource(Res.string.navigation_games))
            },
        )
        NavigationBarItem(
            selected = currentRoute == Route.BackloggedGameList,
            onClick = { onRouteSelected(Route.BackloggedGameList) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(Res.string.navigation_backlog),
                )
            },
            label = {
                Text(text = stringResource(Res.string.navigation_backlog))
            },
        )
    }
}

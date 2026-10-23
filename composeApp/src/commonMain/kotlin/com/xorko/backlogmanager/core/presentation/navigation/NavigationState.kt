package com.xorko.backlogmanager.core.presentation.navigation

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.serialization.SavedStateConfiguration
import com.xorko.backlogmanager.app.Route
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * Create a navigation state that persists config changes and process death.
 *
 * @param startKey - the starting top level key.
 * @param topLevelKeys - the top level keys, each of which gets its own substack.
 * @param deepLinkKey - optional deep link key to use as the initial destination of the [startKey]
 * @param configuration - the [SavedStateConfiguration] to use.
 */
@Composable
fun rememberNavigationState(
    startKey: NavKey,
    topLevelKeys: Set<NavKey>,
    deepLinkKey: NavKey? = null,
    configuration: SavedStateConfiguration =
        remember {
            SavedStateConfiguration {
                serializersModule =
                    SerializersModule {
                        polymorphic(NavKey::class) {
                            subclass(Route.GameList::class, Route.GameList.serializer())
                            subclass(Route.BackloggedGameList::class, Route.BackloggedGameList.serializer())
                            subclass(Route.GameDetail::class, Route.GameDetail.serializer())
                        }
                    }
            }
        },
): NavigationState {
    val topLevelStack = rememberNavBackStack(configuration, startKey)
    val subStacks =
        topLevelKeys.associateWith { key ->
            val firstKey = if (key == startKey && deepLinkKey != null) deepLinkKey else key
            rememberNavBackStack(configuration, firstKey)
        }

    return remember(startKey, topLevelKeys) {
        NavigationState(
            startKey = startKey,
            topLevelStack = topLevelStack,
            subStacks = subStacks,
        )
    }
}

/**
 * State holder for navigation state.
 *
 * @param startKey - the starting navigation key. The user will exit the app through this key.
 * @param topLevelStack - the top level back stack. It holds only top level keys.
 * @param subStacks - the back stacks for each top level key
 */
class NavigationState(
    val startKey: NavKey,
    val topLevelStack: NavBackStack<NavKey>,
    val subStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    val currentTopLevelKey: NavKey by derivedStateOf { topLevelStack.last() }

    val topLevelKeys
        get() = subStacks.keys

    @get:VisibleForTesting
    val currentSubStack: NavBackStack<NavKey>
        get() =
            subStacks[currentTopLevelKey]
                ?: error("Sub stack for $currentTopLevelKey does not exist")

    @get:VisibleForTesting
    val currentKey: NavKey by derivedStateOf { currentSubStack.last() }

    val isAtTopLevel: Boolean by derivedStateOf { currentSubStack.size == 1 }
}

/**
 * Convert NavigationState into NavEntries.
 */
@Composable
fun NavigationState.toEntries(entryProvider: (NavKey) -> NavEntry<NavKey>): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries =
        subStacks.mapValues { (_, stack) ->
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators =
                    listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                entryProvider = entryProvider,
            )
        }

    return topLevelStack
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}

package com.xorko.backlogmanager.core.presentation

import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.shouldLoadMoreItems(threshold: Int = 3): Boolean {
    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return false
    return lastVisibleItemIndex >= layoutInfo.totalItemsCount - threshold
}

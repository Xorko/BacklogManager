package com.xorko.backlogmanager.core.presentation

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed interface UiText {
    data class DynamicString(
        val value: String,
    ) : UiText

    class StringResourceId(
        val id: StringResource,
        val args: Array<Any> = emptyArray(),
    ) : UiText

    @Composable
    fun asString(): String =
        when (this) {
            is DynamicString -> this.value
            is StringResourceId -> stringResource(resource = id, formatArgs = args)
        }
}

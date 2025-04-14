package com.xorko.backlogmanager.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
    darkColorScheme(
        primary = Primary,
        onPrimary = Color.White,
        background = Background,
        onBackground = TextPrimary,
        surface = Surface,
        onSurface = TextPrimary,
        error = Error,
        onError = Color.White,
    )

@Composable
fun BacklogManagerTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        shapes = BacklogManagerShapes,
        content = content,
    )
}

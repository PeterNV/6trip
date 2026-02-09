package com.example.a6trip.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = White,
    secondary = TextSecondary,
    tertiary = TextTertiary,
    background = Black,
    surface = TextPrimary,
    onPrimary = Black,
    onSecondary = White,
    onBackground = White,
    onSurface = White
)

private val LightColorScheme = lightColorScheme(
    primary = Black,
    secondary = TextPrimary,
    tertiary = TextSecondary,
    background = White,
    surface = SurfaceLight,
    onPrimary = White,
    onSecondary = Black,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun _6tripTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
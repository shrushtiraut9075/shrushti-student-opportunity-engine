package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = OxCyanLight,
    onPrimary = Color(0xFF082F49),
    primaryContainer = Color(0xFF0369A1),
    onPrimaryContainer = Color(0xFFE0F2FE),
    secondary = OxIndigoLight,
    onSecondary = Color(0xFF1E1B4B),
    secondaryContainer = Color(0xFF3730A3),
    onSecondaryContainer = Color(0xFFEEF2FF),
    tertiary = OxEmerald,
    onTertiary = Color(0xFF022C22),
    background = OxDarkBackground,
    onBackground = OxDarkTextPrimary,
    surface = OxDarkSurface,
    onSurface = OxDarkTextPrimary,
    surfaceVariant = OxDarkSurfaceVariant,
    onSurfaceVariant = OxDarkTextSecondary,
    outline = OxDarkBorder,
    error = OxRose,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = OxCyan,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = OxIndigo,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEEF2FF),
    onSecondaryContainer = Color(0xFF312E81),
    tertiary = OxEmerald,
    onTertiary = Color.White,
    background = OxLightBackground,
    onBackground = OxLightTextPrimary,
    surface = OxLightSurface,
    onSurface = OxLightTextPrimary,
    surfaceVariant = OxLightSurfaceVariant,
    onSurfaceVariant = OxLightTextSecondary,
    outline = OxLightBorder,
    error = OxRose,
    onError = Color.White
)

@Composable
fun OpportunityXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Alias for compatibility
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    OpportunityXTheme(darkTheme = darkTheme, content = content)
}

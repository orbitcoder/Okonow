package com.noitacilppa.okonow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPurple,
    primaryContainer = PrimaryContainer,
    secondary = SecondaryTeal,
    tertiary = TertiaryPink,
    background = Background,
    surface = Surface,
    onPrimary = OnSurface,
    onSecondary = Background,
    onTertiary = Background,
    onBackground = OnSurface,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
    outlineVariant = OutlineVariant,
    error = ErrorCoral,
    onError = OnErrorDark
)

@Composable
fun OkonowTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
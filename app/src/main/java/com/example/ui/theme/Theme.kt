package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val CineNovaDarkColorScheme = darkColorScheme(
    primary = CrimsonAccent,
    onPrimary = TextPrimary,
    primaryContainer = CrimsonContainer,
    onPrimaryContainer = TextPrimary,
    secondary = AmberGold,
    onSecondary = ObsidianVoid,
    secondaryContainer = AmberContainer,
    onSecondaryContainer = AmberGold,
    tertiary = CyanSignal,
    onTertiary = ObsidianVoid,
    tertiaryContainer = CyanContainer,
    onTertiaryContainer = CyanSignal,
    background = ObsidianVoid,
    onBackground = TextPrimary,
    surface = ObsidianSurface,
    onSurface = TextPrimary,
    surfaceVariant = ObsidianSurfaceHigh,
    onSurfaceVariant = TextSecondary,
    surfaceContainerLowest = ObsidianVoid,
    surfaceContainerLow = ObsidianSurface,
    surfaceContainer = ObsidianSurfaceHigh,
    surfaceContainerHigh = ObsidianSurfaceBright,
    outline = BorderSubtle,
    outlineVariant = BorderSubtle,
    error = CrimsonAccent,
    onError = TextPrimary
)

val CineNovaShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun CineNovaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CineNovaDarkColorScheme,
        typography = CineNovaTypography,
        shapes = CineNovaShapes,
        content = content
    )
}

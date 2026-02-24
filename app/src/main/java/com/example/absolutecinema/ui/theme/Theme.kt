package com.example.absolutecinema.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.core.ui.ThemeStyle

private val LightColorPalette = lightColorScheme(
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF2F2F2),
    surfaceVariant = Color(0xFFE4E4E4),
    primary = Color(0xFF262626),
    secondary = Color(0xFF6A6A6A),
    outline = Color(0xFFD0D0D0),
)

private val DarkColorPalette = darkColorScheme(
    background = Color(0xFF1E1E1E),
    surface = Color(0xFF323232),
    surfaceVariant = Color(0xFF3B3B3B),
    primary = Color(0xFFFFFFFF),
    secondary = Color(0xFFC1C0C0),
    outline = Color(0xFF575757),
)

private val UltramarineColorPalette = darkColorScheme(
    background = Color(0xFF101833),
    surface = Color(0xFF18234A),
    surfaceVariant = Color(0xFF233261),
    primary = Color(0xFFF2F4FF),
    secondary = Color(0xFFB9C4EA),
    outline = Color(0xFF445892),
)

private val GraphiteColorPalette = darkColorScheme(
    background = Color(0xFF181A1D),
    surface = Color(0xFF22262B),
    surfaceVariant = Color(0xFF2E3339),
    primary = Color(0xFFF1F3F5),
    secondary = Color(0xFFBDC3CA),
    outline = Color(0xFF4E545B),
)

private val SoftLightColorPalette = lightColorScheme(
    background = Color(0xFFFFFAF2),
    surface = Color(0xFFF3E9D8),
    surfaceVariant = Color(0xFFECE0CD),
    primary = Color(0xFF2F2A26),
    secondary = Color(0xFF6F655D),
    outline = Color(0xFFD8C7AF),
)

@Composable
fun AbsoluteCinemaTheme(
    themeStyle: ThemeStyle = ThemeStyle.Light,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeStyle) {
        ThemeStyle.Light -> LightColorPalette
        ThemeStyle.Dark -> DarkColorPalette
        ThemeStyle.Ultramarine -> UltramarineColorPalette
        ThemeStyle.Graphite -> GraphiteColorPalette
        ThemeStyle.SoftLight -> SoftLightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
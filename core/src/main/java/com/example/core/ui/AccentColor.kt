package com.example.core.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.math.max
import kotlin.math.min

val LocalAccentColor = compositionLocalOf { Color(0xFFFF8000) }

private const val DEFAULT_ACCENT_HEX = "#FF8000"
private const val MIN_ACCENT_CONTRAST = 2.2f

val AccentHexOptions = listOf(
    "#FF8000",
    "#CD4A4C",
    "#9B2D30",
    "#FF8C42",
    "#FFB84C",
    "#F26B38",
    "#5D76CB",
    "#4169E1",
)

/**
 * Parses a 6-digit HEX string (with or without leading #) to [Color].
 * Falls back to default orange if parsing fails.
 */
fun parseAccentHex(hex: String): Color {
    val clean = hex.trim().removePrefix("#").take(6)
    if (clean.length != 6) return Color(0xFFFF8000)
    return try {
        val r = clean.substring(0, 2).toInt(16) / 255f
        val g = clean.substring(2, 4).toInt(16) / 255f
        val b = clean.substring(4, 6).toInt(16) / 255f
        Color(red = r, green = g, blue = b)
    } catch (_: Exception) {
        Color(0xFFFF8000)
    }
}

/**
 * Formats [Color] to 6-digit HEX string with leading # (e.g. "#FF8000").
 */
fun formatAccentHex(color: Color): String {
    val r = (color.red * 255).toInt().coerceIn(0, 255)
    val g = (color.green * 255).toInt().coerceIn(0, 255)
    val b = (color.blue * 255).toInt().coerceIn(0, 255)
    return "#%02X%02X%02X".format(r, g, b)
}

private fun contrastRatio(first: Color, second: Color): Float {
    val l1 = first.luminance()
    val l2 = second.luminance()
    val lighter = max(l1, l2)
    val darker = min(l1, l2)
    return ((lighter + 0.05f) / (darker + 0.05f))
}

private fun themeBackgrounds(style: ThemeStyle): List<Color> {
    return when (style) {
        ThemeStyle.Light -> listOf(Color(0xFFFFFFFF), Color(0xFFF2F2F2))
        ThemeStyle.Dark -> listOf(Color(0xFF1E1E1E), Color(0xFF323232))
        ThemeStyle.Ultramarine -> listOf(Color(0xFF101833), Color(0xFF18234A))
        ThemeStyle.Graphite -> listOf(Color(0xFF181A1D), Color(0xFF22262B))
        ThemeStyle.SoftLight -> listOf(Color(0xFFFFFAF2), Color(0xFFF3E9D8))
    }
}

fun isAccentCompatibleWithTheme(accentHex: String, themeStyle: ThemeStyle): Boolean {
    val accent = parseAccentHex(accentHex)
    val backgrounds = themeBackgrounds(themeStyle)
    return backgrounds.all { bg -> contrastRatio(accent, bg) >= MIN_ACCENT_CONTRAST }
}

fun normalizeAccentForTheme(
    preferredAccentHex: String,
    themeStyle: ThemeStyle,
    options: List<String> = AccentHexOptions,
): String {
    val normalized = preferredAccentHex.uppercase()
    if (isAccentCompatibleWithTheme(normalized, themeStyle)) return normalized
    return options.firstOrNull { isAccentCompatibleWithTheme(it, themeStyle) } ?: DEFAULT_ACCENT_HEX
}

fun nextCompatibleAccentHex(
    currentAccentHex: String,
    themeStyle: ThemeStyle,
    options: List<String> = AccentHexOptions,
): String {
    if (options.isEmpty()) return DEFAULT_ACCENT_HEX
    val currentIndex = options.indexOfFirst { it.equals(currentAccentHex, ignoreCase = true) }
        .takeIf { it >= 0 } ?: 0
    for (step in 1..options.size) {
        val candidate = options[(currentIndex + step) % options.size]
        if (isAccentCompatibleWithTheme(candidate, themeStyle)) {
            return candidate
        }
    }
    return normalizeAccentForTheme(currentAccentHex, themeStyle, options)
}

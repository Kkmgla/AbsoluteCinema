package com.example.core.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalAccentColor = compositionLocalOf { Color(0xFFFF8000) }

private const val DEFAULT_ACCENT_HEX = "#FF8000"

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

package com.example.core.ui

enum class ThemeStyle(val storageValue: String) {
    Light("light"),
    Dark("dark"),
    Ultramarine("ultramarine"),
    Graphite("graphite"),
    SoftLight("soft_light");

    companion object {
        fun fromStorageValue(value: String?): ThemeStyle {
            return values().firstOrNull { it.storageValue == value } ?: Light
        }
    }
}

fun ThemeStyle.nextStyle(): ThemeStyle {
    val all = ThemeStyle.values()
    return all[(ordinal + 1) % all.size]
}

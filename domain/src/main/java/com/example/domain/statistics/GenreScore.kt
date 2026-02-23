package com.example.domain.statistics

/**
 * Result of weighted genre score calculation (User Preference Index).
 * @param genreName Display name of the genre (e.g. from API).
 * @param score Weighted score, rounded to 2 decimals.
 */
data class GenreScore(
    val genreName: String,
    val score: Double,
)

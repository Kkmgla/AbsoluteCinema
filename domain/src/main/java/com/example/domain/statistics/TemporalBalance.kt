package com.example.domain.statistics

/**
 * Result of temporal balance calculation (average year and modernity index).
 *
 * @param averageYear Rounded average release year of watched movies.
 * @param modernityIndex Normalized 0–1: 0 = classic preference, 1 = modern preference.
 * @param varianceYears Optional variance of years (spread); null if not computed.
 */
data class TemporalBalance(
    val averageYear: Int,
    val modernityIndex: Double,
    val varianceYears: Double? = null,
)

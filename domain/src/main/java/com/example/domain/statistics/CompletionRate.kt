package com.example.domain.statistics

/**
 * Result of completion rate calculation: share of started movies that were watched to the end.
 * C = W / (W + D), where W = watched count, D = dropped count.
 *
 * @param watchedCount Number of movies watched to the end.
 * @param droppedCount Number of movies dropped (not finished).
 * @param totalStarted watchedCount + droppedCount.
 * @param completionRatio C in 0..1.
 * @param completionPercent C as integer 0..100 for display.
 */
data class CompletionRate(
    val watchedCount: Int,
    val droppedCount: Int,
    val totalStarted: Int,
    val completionRatio: Double,
    val completionPercent: Int,
)

package com.example.domain.model

/**
 * Информация о рецензиях к фильму (количество, доля положительных).
 */
data class ReviewInfo(
    var count: Int? = null,
    var positiveCount: Int? = null,
    var percentage: String? = null,
)

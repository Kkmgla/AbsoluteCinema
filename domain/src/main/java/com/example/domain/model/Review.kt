package com.example.domain.model

data class Review(
    var id: Int? = null,
    var movieId: Int? = null,
    var title: String? = null,
    var review: String? = null,
    var author: String? = null,
    var type: String? = null,
    var date: String? = null,
)

package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName

data class ReviewDto(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("movieId") var movieId: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("review") var review: String? = null,
    @SerializedName("author") var author: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("date") var date: String? = null,
)

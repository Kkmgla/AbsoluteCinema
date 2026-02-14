package com.example.data.remote.dto.responce

import com.example.data.remote.dto.common.MovieAwardDto
import com.google.gson.annotations.SerializedName

data class MovieAwardsResponseDto(
    @SerializedName("docs") var docs: List<MovieAwardDto> = emptyList(),
    @SerializedName("total") var total: Int? = null,
    @SerializedName("limit") var limit: Int? = null,
    @SerializedName("page") var page: Int? = null,
    @SerializedName("pages") var pages: Int? = null,
)

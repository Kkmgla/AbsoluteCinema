package com.example.data.remote.dto.responce

import com.example.data.remote.dto.common.ImageDto
import com.google.gson.annotations.SerializedName

data class ImageResponseDto(
    @SerializedName("docs") var docs: List<ImageDto> = emptyList(),
    @SerializedName("total") var total: Double? = null,
    @SerializedName("limit") var limit: Double? = null,
    @SerializedName("page") var page: Double? = null,
    @SerializedName("pages") var pages: Double? = null,
)

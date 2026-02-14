package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("url") var url: String? = null,
    @SerializedName("previewUrl") var previewUrl: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("height") var height: Double? = null,
    @SerializedName("width") var width: Double? = null,
)

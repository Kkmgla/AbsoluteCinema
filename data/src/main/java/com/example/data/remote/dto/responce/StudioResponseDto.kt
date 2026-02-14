package com.example.data.remote.dto.responce

import com.example.data.remote.dto.common.StudioDto
import com.google.gson.annotations.SerializedName

data class StudioResponseDto(
    @SerializedName("docs") var docs: List<StudioDto> = emptyList(),
    @SerializedName("total") var total: Int? = null,
    @SerializedName("limit") var limit: Int? = null,
    @SerializedName("page") var page: Int? = null,
    @SerializedName("pages") var pages: Int? = null,
)

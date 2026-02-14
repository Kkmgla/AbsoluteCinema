package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName

data class MovieAwardDto(
    @SerializedName("nomination") var nomination: NominationDto? = null,
    @SerializedName("winning") var winning: Boolean? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("year") var year: Int? = null,
)

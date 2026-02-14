package com.example.data.remote.dto.common

import com.google.gson.annotations.SerializedName

data class NominationDto(
    @SerializedName("title") var title: String? = null,
    @SerializedName("year") var year: Int? = null,
)

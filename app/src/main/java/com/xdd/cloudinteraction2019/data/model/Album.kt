package com.xdd.cloudinteraction2019.data.model

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("userId") val userId: Long,
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String
)
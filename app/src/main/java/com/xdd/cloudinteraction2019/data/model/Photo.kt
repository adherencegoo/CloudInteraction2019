package com.xdd.cloudinteraction2019.data.model

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("albumId") val albumId: Long,
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String
) {
    companion object {
        val diffCallback = object : ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem == newItem
        }
    }
}

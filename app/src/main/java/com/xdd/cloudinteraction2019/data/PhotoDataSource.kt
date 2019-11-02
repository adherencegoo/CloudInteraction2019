package com.xdd.cloudinteraction2019.data

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.xdd.cloudinteraction2019.data.model.Album
import com.xdd.cloudinteraction2019.data.model.Photo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PhotoDataSource private constructor() : PageKeyedDataSource<Long, Photo>() {
    class Factory : DataSource.Factory<Long, Photo>() {
        override fun create(): DataSource<Long, Photo> = PhotoDataSource()
    }

    private val albumIds = ArrayList<Long>()

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Photo>
    ) {
        // Load all albums, because we can't assume that albumIds are continuous
        RetrofitClient.api.getAlbums().enqueue(object : Callback<List<Album>> {
            override fun onFailure(call: Call<List<Album>>, t: Throwable) {
                albumIds.clear()
                callback.onResult(emptyList(), null, null)
            }

            override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {
                response.body()?.map { it.id }?.let {
                    albumIds.clear()
                    albumIds.addAll(it)
                    loadInitialOnAlbumReady(callback)
                }
            }
        })
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Photo>) {
        loadWithOffset(params, callback, 1)
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Photo>) {
        loadWithOffset(params, callback, -1)
    }

    private fun loadInitialOnAlbumReady(callback: LoadInitialCallback<Long, Photo>) {
        albumIds.firstOrNull()?.let {
            RetrofitClient.api.getPhotos(albumId = it).enqueue(object : Callback<List<Photo>> {
                override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                    callback.onResult(response.body() ?: emptyList(), null, albumIds.getOrNull(1))
                }

                override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                    callback.onResult(emptyList(), null, null)
                }
            })
        } ?: kotlin.run {
            callback.onResult(emptyList(), null, null)
        }
    }

    private fun loadWithOffset(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, Photo>,
        indexOffset: Int
    ) {
        val currentIndex = albumIds.indexOf(params.key)
        val neighborAlbumId = if (currentIndex == -1) {
            null
        } else {
            albumIds.getOrNull(currentIndex + indexOffset)
        }

        RetrofitClient.api.getPhotos(albumId = params.key).enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                callback.onResult(response.body() ?: emptyList(), neighborAlbumId)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {}
        })
    }
}
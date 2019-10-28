package com.xdd.cloudinteraction2019.data

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.xdd.cloudinteraction2019.data.model.Photo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PhotoDataSource private constructor(): PageKeyedDataSource<Long, Photo>() {
    class Factory : DataSource.Factory<Long, Photo>() {
        override fun create(): DataSource<Long, Photo> = PhotoDataSource()
    }

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Photo>
    ) {
        RetrofitClient.api.getPhotos(albumId = 1).enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                callback.onResult(response.body() ?: emptyList(), null, 2L)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {}
        })
    }

    override fun loadAfter(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, Photo>
    ) {
        RetrofitClient.api.getPhotos(albumId = params.key).enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                callback.onResult(response.body() ?: emptyList(), params.key + 1)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {}
        })
    }

    override fun loadBefore(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, Photo>
    ) {
        RetrofitClient.api.getPhotos(albumId = params.key).enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                callback.onResult(response.body() ?: emptyList(), params.key - 1)
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {}
        })
    }
}
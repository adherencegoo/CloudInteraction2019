package com.xdd.cloudinteraction2019.data

import com.xdd.cloudinteraction2019.data.model.Album
import com.xdd.cloudinteraction2019.data.model.Photo
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


object RetrofitClient {
    private const val BASE_URI = "https://jsonplaceholder.typicode.com"

    interface Api {
        @GET("/photos")
        fun getPhotos(
            @Query("albumId") albumId: Long? = null,
            @Query("id") photoId: Long? = null
        ): Call<List<Photo>>

        @GET("/albums")
        fun getAlbums(
            @Query("albumId") albumId: Long? = null,
            @Query("id") photoId: Long? = null
        ): Call<List<Album>>
    }

    val api: Api = Retrofit.Builder()
        .baseUrl(BASE_URI)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)
}
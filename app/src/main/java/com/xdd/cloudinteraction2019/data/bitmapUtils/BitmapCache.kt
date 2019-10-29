package com.xdd.cloudinteraction2019.data.bitmapUtils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.collection.LruCache
import androidx.lifecycle.MutableLiveData
import okhttp3.*
import java.io.IOException
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.Executors

object BitmapCache {
    private val memoryCache: LruCache<String, BitmapRequest> =
        object : LruCache<String, BitmapRequest>(
            (Runtime.getRuntime().maxMemory() * 0.8).toInt()
        ) {
            override fun sizeOf(key: String, bitmapRequest: BitmapRequest): Int {
                return (bitmapRequest.response?.byteCount) ?: 0
            }

            override fun entryRemoved(
                evicted: Boolean,
                key: String,
                oldValue: BitmapRequest,
                newValue: BitmapRequest?
            ) {
                if (evicted) {
                    oldValue.response?.recycle()
                }
            }
        }

    private val httpClient = OkHttpClient.Builder().dispatcher(
        Dispatcher(Executors.newFixedThreadPool(15))
    ).build()

    private val pendingRequests =
        HashMap<String/*url*/, CopyOnWriteArraySet<MutableLiveData<BitmapRequest>>>()

    /**
     * @return `true`: has cache
     * */
    fun requestBitmap(bitmapRequest: BitmapRequest, liveBitmap: MutableLiveData<BitmapRequest>): Boolean {
        return loadFromCache(bitmapRequest.url)?.let {
            liveBitmap.postValue(it)
            true
        } ?: run {
            var isNewRequest = false
            val pendingLives = synchronized(pendingRequests) {
                pendingRequests.getOrPut(bitmapRequest.url) {
                    isNewRequest = true
                    CopyOnWriteArraySet()
                }
            }
            pendingLives += liveBitmap

            if (isNewRequest) { // New request
                val request = Request.Builder().url(bitmapRequest.url).build()
                httpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        onNewBitmapLoaded(bitmapRequest, null)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val bitmap = response.body()?.let { body ->
                            BitmapFactory.decodeStream(body.byteStream())
                        }

                        onNewBitmapLoaded(bitmapRequest, bitmap)
                    }
                })
            }

            // Make sure that no any LiveData is missed
            loadFromCache(bitmapRequest.url)?.let { request ->
                pendingLives.forEach { liveData ->
                    liveData.postValue(request)
                }

                true
            } ?: false
        }
    }

    private fun onNewBitmapLoaded(bitmapRequest: BitmapRequest, bitmap: Bitmap?) {
        bitmapRequest.response = bitmap
        memoryCache.put(bitmapRequest.url, bitmapRequest)

        synchronized(pendingRequests) {
            pendingRequests.remove(bitmapRequest.url)
        }?.forEach {
            it.postValue(bitmapRequest)
        }
    }

    private fun loadFromCache(url: String): BitmapRequest?
            = memoryCache[url]?.takeIf(BitmapRequest::isResponseValid)
}
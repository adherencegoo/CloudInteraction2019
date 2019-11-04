package com.xdd.cloudinteraction2019.data.bitmapUtils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.collection.LruCache
import androidx.lifecycle.MutableLiveData
import okhttp3.*
import java.io.IOException
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object BitmapCache {
    internal class PendingEntry {
        internal var lastRequestedTime = 0L
        internal val liveBitmaps = CopyOnWriteArraySet<MutableLiveData<Bitmap>>()

        internal fun updateTime() {
            lastRequestedTime = System.currentTimeMillis()
        }
    }

    private val memoryCache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(
        (Runtime.getRuntime().maxMemory() * 0.8).toInt()
    ) {
        override fun sizeOf(key: String, bitmap: Bitmap): Int {
            return bitmap.byteCount
        }

        override fun entryRemoved(
            evicted: Boolean,
            key: String,
            oldValue: Bitmap,
            newValue: Bitmap?
        ) {
            if (evicted) {
                oldValue.recycle()
            }
        }
    }

    private val pendingRequests = HashMap<String/*url*/, PendingEntry>()

    private val methodGetRealCall = Class.forName("okhttp3.RealCall\$AsyncCall")
        .getDeclaredMethod("get").apply {
            isAccessible = true
        }

    private fun Runnable.getLastRequestedTime(): Long? {
        val call = (methodGetRealCall.invoke(this) as? Call) ?: return null
        val url  = call.request().url().toString()
        return synchronized(pendingRequests) {
            pendingRequests[url]?.lastRequestedTime
        }
    }

    private val priorityBlockingQueue = PriorityBlockingQueue<Runnable>(1000, Comparator<Runnable> { r1, r2 ->
        val time1 = r1.getLastRequestedTime()
        val time2 = r2.getLastRequestedTime()

        if (time1 != null && time2 != null) {
            time2.compareTo(time1) // Descending order
        } else {
            -1
        }
    })

    private val httpClient = OkHttpClient.Builder().dispatcher(
        Dispatcher(
            ThreadPoolExecutor(
                5,
                10,
                1,
                TimeUnit.MINUTES,
                priorityBlockingQueue
            )
        ).apply {
            maxRequests = 1000
            maxRequestsPerHost = 1000
        }
    ).build()

    /**
     * @return `true`: has cache
     * */
    fun requestBitmap(url: String, liveBitmap: MutableLiveData<Bitmap>): Boolean {
        return loadFromCache(url)?.let {
            liveBitmap.postValue(it)
            true
        } ?: run {
            var isNewRequest = false
            val pendingEntry = synchronized(pendingRequests) {
                pendingRequests.getOrPut(url) {
                    isNewRequest = true
                    PendingEntry()
                }
            }
            pendingEntry.liveBitmaps += liveBitmap
            pendingEntry.updateTime()

            if (isNewRequest) { // New request
                val request = Request.Builder().url(url).build()
                httpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        onNewBitmapLoaded(url, null)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val bitmap = response.body()?.let { body ->
                            BitmapFactory.decodeStream(body.byteStream())
                        }

                        onNewBitmapLoaded(url, bitmap)
                    }
                })
            }

            // Make sure that no any LiveData is missed
            loadFromCache(url)?.let { bitmap ->
                pendingEntry.liveBitmaps.forEach { liveData ->
                    liveData.postValue(bitmap)
                }

                true
            } ?: false
        }
    }

    private fun onNewBitmapLoaded(url: String, bitmap: Bitmap?) {
        // Save to cache
        bitmap?.let {
            memoryCache.put(url, it)
        } ?: kotlin.run {
            memoryCache.remove(url)
        }

        // Notify listeners
        synchronized(pendingRequests) {
            pendingRequests.remove(url)
        }?.liveBitmaps?.forEach {
            it.postValue(bitmap)
        }
    }

    private fun loadFromCache(url: String): Bitmap? =
        memoryCache[url]?.takeUnless(Bitmap::isRecycled)
}
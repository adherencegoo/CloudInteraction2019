package com.xdd.cloudinteraction2019.ui.second

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.xdd.cloudinteraction2019.data.PhotoDataSource
import com.xdd.cloudinteraction2019.data.model.Photo
import java.util.*
import java.util.concurrent.Executors

class SecondViewModel : ViewModel() {
    companion object {
        private val pagedListConfig = PagedList.Config.Builder()
            .setInitialLoadSizeHint(10)
            .setPageSize(20)
            .setPrefetchDistance(8)
            .build()
    }

    private val _liveLoading = MutableLiveData<Boolean>()
    val liveLoading: LiveData<Boolean> = _liveLoading

    val livePhotos = LivePagedListBuilder<Long, Photo>(
        PhotoDataSource.Factory(_liveLoading),
        pagedListConfig
    ).setFetchExecutor(Executors.newFixedThreadPool(5)).build()

    /**
     * key: url
     * */
    private val liveBitmapRequests = HashMap<String, MutableLiveData<Bitmap>>()

    fun getLiveBitmapRequest(url: String) = liveBitmapRequests.getOrPut(url) { MutableLiveData() }
}
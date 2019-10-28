package com.xdd.cloudinteraction2019.ui.second

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.xdd.cloudinteraction2019.data.PhotoDataSource
import com.xdd.cloudinteraction2019.data.model.Photo
import java.util.concurrent.Executors

class SecondViewModel : ViewModel() {
    companion object {
        private val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPageSize(20)
            .setPrefetchDistance(8)
            .build()
    }

    val livePhotos = LivePagedListBuilder<Long, Photo>(
        PhotoDataSource.Factory(),
        pagedListConfig
    ).setFetchExecutor(Executors.newFixedThreadPool(5)).build()
}
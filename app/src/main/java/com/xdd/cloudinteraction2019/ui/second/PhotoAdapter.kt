package com.xdd.cloudinteraction2019.ui.second

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xdd.cloudinteraction2019.R
import com.xdd.cloudinteraction2019.data.bitmapUtils.BitmapCache
import com.xdd.cloudinteraction2019.data.bitmapUtils.BitmapRequest
import com.xdd.cloudinteraction2019.data.bitmapUtils.FilteredLiveBitmap
import com.xdd.cloudinteraction2019.data.model.Photo
import com.xdd.cloudinteraction2019.databinding.PhotoCellBinding

class PhotoAdapter : PagedListAdapter<Photo, PhotoAdapter.ViewHolder>(Photo.diffCallback) {
    class ViewHolder(internal val cellBinding: PhotoCellBinding) : RecyclerView.ViewHolder(cellBinding.root) {
        private var requestedUrl = ""
        private val mutableLiveBitmap = MutableLiveData<BitmapRequest>()

        init {
            cellBinding.liveBitmap = FilteredLiveBitmap(mutableLiveBitmap) {
                requestedUrl == it.url
            }
        }

        @UiThread
        fun requestBitmap(url: String) {
            requestedUrl = url
            if (!BitmapCache.requestBitmap(BitmapRequest(url), mutableLiveBitmap)) {
                cellBinding.imageView.setImageResource(R.drawable.ic_picture_placeholder)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PhotoCellBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            lifecycleOwner = parent.context as? LifecycleOwner
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoData = getItem(position)
        holder.cellBinding.photo = photoData

        //todo use url of full image
        photoData?.thumbnailUrl?.let {
            holder.requestBitmap(it)
        }
    }
}
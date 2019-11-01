package com.xdd.cloudinteraction2019.ui.second

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xdd.cloudinteraction2019.data.bitmapUtils.BitmapCache
import com.xdd.cloudinteraction2019.data.bitmapUtils.BitmapRequest
import com.xdd.cloudinteraction2019.data.model.Photo
import com.xdd.cloudinteraction2019.databinding.PhotoCellBinding

class PhotoAdapter(private val viewModel: SecondViewModel) :
    PagedListAdapter<Photo, PhotoAdapter.ViewHolder>(Photo.diffCallback) {
    class ViewHolder(internal val cellBinding: PhotoCellBinding) :
        RecyclerView.ViewHolder(cellBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PhotoCellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).apply {
                lifecycleOwner = parent.context as? LifecycleOwner
            })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { photo ->
            holder.cellBinding.photo = photo

            val liveBitmap = viewModel.getLiveBitmapRequest(photo.url)
            holder.cellBinding.liveBitmap = liveBitmap
            BitmapCache.requestBitmap(BitmapRequest(photo.thumbnailUrl), liveBitmap)
        }
    }
}
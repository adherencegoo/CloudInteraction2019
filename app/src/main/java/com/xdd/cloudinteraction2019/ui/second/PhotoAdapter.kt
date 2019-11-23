package com.xdd.cloudinteraction2019.ui.second

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xdd.cloudinteraction2019.R
import com.xdd.cloudinteraction2019.data.model.Photo
import com.xdd.cloudinteraction2019.databinding.PhotoCellBinding

class PhotoAdapter : PagedListAdapter<Photo, PhotoAdapter.ViewHolder>(Photo.diffCallback) {

    class ViewHolder(private val cellBinding: PhotoCellBinding) :
        RecyclerView.ViewHolder(cellBinding.root) {

        internal fun bindData(photo: Photo) {
            cellBinding.photo = photo

            Glide.with(cellBinding.root.context)
                .load(photo.thumbnailUrl)
                .placeholder(R.drawable.ic_picture_placeholder)
                .into(cellBinding.imageView)
        }
    }

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
        getItem(position)?.let {
            holder.bindData(it)
        }
    }
}
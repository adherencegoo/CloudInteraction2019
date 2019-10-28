package com.xdd.cloudinteraction2019.ui.second

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xdd.cloudinteraction2019.data.model.Photo
import com.xdd.cloudinteraction2019.databinding.PhotoCellBinding

class PhotoAdapter : PagedListAdapter<Photo, PhotoAdapter.ViewHolder>(Photo.diffCallback) {
    class ViewHolder(internal val cellBinding: PhotoCellBinding) : RecyclerView.ViewHolder(cellBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PhotoCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cellBinding.photo = getItem(position)
    }
}
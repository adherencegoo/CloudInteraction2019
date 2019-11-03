package com.xdd.cloudinteraction2019.ui

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.xdd.cloudinteraction2019.R

@BindingAdapter("android:setBitmap")
fun ImageView.setBitmap(bitmap: Bitmap?) {
    bitmap?.takeUnless(Bitmap::isRecycled)?.let {
        setImageBitmap(it)
    } ?: kotlin.run {
        setImageResource(R.drawable.ic_picture_placeholder)
    }
}
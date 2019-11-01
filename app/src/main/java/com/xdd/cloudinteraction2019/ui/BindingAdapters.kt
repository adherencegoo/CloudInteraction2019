package com.xdd.cloudinteraction2019.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.xdd.cloudinteraction2019.R
import com.xdd.cloudinteraction2019.data.bitmapUtils.BitmapRequest

@BindingAdapter("android:setBitmapRequest")
fun ImageView.setBitmapRequest(bitmapRequest: BitmapRequest?) {
    bitmapRequest?.takeIf { it.isResponseValid() }?.let {
        setImageBitmap(it.response)
    } ?: kotlin.run {
        setImageResource(R.drawable.ic_picture_placeholder)
    }
}
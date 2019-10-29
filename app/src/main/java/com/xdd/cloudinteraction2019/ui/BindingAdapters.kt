package com.xdd.cloudinteraction2019.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.xdd.cloudinteraction2019.data.bitmapUtils.BitmapRequest

@BindingAdapter("android:setBitmapRequest")
fun ImageView.setBitmapRequest(imageRequest: BitmapRequest?) {
    imageRequest?.takeIf { it.isResponseValid() }?.let {
        setImageBitmap(it.response)
    }
}
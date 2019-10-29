package com.xdd.cloudinteraction2019.data.bitmapUtils

import android.graphics.Bitmap

data class BitmapRequest(val url: String, var response: Bitmap? = null) {
    fun isResponseValid() = response?.isRecycled == false
}
package com.xdd.cloudinteraction2019.data.bitmapUtils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class FilteredLiveBitmap(srcLiveData: LiveData<BitmapRequest>,
                         bitmapRequestValidator: (BitmapRequest) -> Boolean) :
    MediatorLiveData<BitmapRequest>() {

    init {
        addSource(srcLiveData) {
            if (it != null) {
                if (value == null || bitmapRequestValidator.invoke(it)) {
                    value = it
                }
            }
        }
    }
}
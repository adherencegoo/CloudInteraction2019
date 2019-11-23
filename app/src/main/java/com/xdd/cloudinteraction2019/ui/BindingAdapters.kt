package com.xdd.cloudinteraction2019.ui

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("android:setVisible")
fun View.setVisible(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.INVISIBLE
}
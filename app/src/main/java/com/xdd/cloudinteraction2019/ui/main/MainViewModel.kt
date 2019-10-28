package com.xdd.cloudinteraction2019.ui.main

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.xdd.cloudinteraction2019.R
import com.xdd.cloudinteraction2019.ui.second.SecondFragment

class MainViewModel : ViewModel() {
    fun goToNext(view: View) {
        val manager = (view.context as? FragmentActivity)?.supportFragmentManager ?: return

        manager.beginTransaction()
            .replace(R.id.container, SecondFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
}

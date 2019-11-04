package com.xdd.cloudinteraction2019.ui.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.xdd.cloudinteraction2019.data.model.Photo
import com.xdd.cloudinteraction2019.databinding.SecondFragmentBinding

class SecondFragment : Fragment() {
    companion object {
        fun newInstance() = SecondFragment()
    }

    private lateinit var fragmentBinding: SecondFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = SecondFragmentBinding.inflate(inflater, container, false)
        fragmentBinding.lifecycleOwner = this

        fragmentBinding.photoRecycler.layoutManager = GridLayoutManager(context, 4)

        return fragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(SecondViewModel::class.java)

        fragmentBinding.viewModel = viewModel
        fragmentBinding.photoRecycler.adapter = PhotoAdapter(viewModel)

        viewModel.livePhotos.observe(this, Observer<PagedList<Photo>> {
            (fragmentBinding.photoRecycler.adapter as PhotoAdapter).submitList(it)
        })
    }
}
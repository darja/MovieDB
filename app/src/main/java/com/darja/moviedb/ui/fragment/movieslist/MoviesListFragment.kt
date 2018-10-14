package com.darja.moviedb.ui.fragment.movieslist

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.darja.moviedb.R
import com.darja.moviedb.ui.fragment.BaseFragment
import com.darja.moviedb.util.DPLog

class MoviesListFragment: BaseFragment<MoviesListViewModel>() {
    private lateinit var view: MoviesListFragmentView

    override fun getViewModel() = MoviesListViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_movies_list, container, false)

        view = MoviesListFragmentView()
        ButterKnife.bind(view, root)

        return root
    }

    override fun onStart() {
        super.onStart()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getSearchResult().observe(this, Observer { DPLog.d("Search finished: %s items", it?.size) })

        viewModel.error.observe(this, Observer {
            if (it != null) {
                view.showEmptyMessage(getString(it))
            }
        })

        viewModel.isRequesting.observe(this, Observer { view.setProgressVisibility(it) })
    }
}
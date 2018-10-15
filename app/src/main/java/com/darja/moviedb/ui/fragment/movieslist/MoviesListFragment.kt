package com.darja.moviedb.ui.fragment.movieslist

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.darja.moviedb.R
import com.darja.moviedb.ui.fragment.BaseFragment

class MoviesListFragment: BaseFragment<MoviesListViewModel>() {
    private lateinit var view: MoviesListFragmentView

    override fun getViewModel() = MoviesListViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_movies_list, container, false)

        view = MoviesListFragmentView()
        ButterKnife.bind(view, root)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view.onActivityCreated(activity)
    }

    override fun onStart() {
        super.onStart()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getSearchResult().observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                view.showMovies(it)
            } else {
                view.showMovies(emptyList())
                view.showEmptyMessage(getString(R.string.error_no_movies_found))
            }
        })

        viewModel.error.observe(this, Observer {
            if (it != null) {
                view.showEmptyMessage(getString(it))
            }
        })

        viewModel.isRequesting.observe(this, Observer { view.setProgressVisibility(it) })
    }
}
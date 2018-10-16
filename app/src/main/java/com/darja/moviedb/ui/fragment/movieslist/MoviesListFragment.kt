package com.darja.moviedb.ui.fragment.movieslist

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import com.darja.moviedb.R
import com.darja.moviedb.ui.MovieSelected
import com.darja.moviedb.ui.fragment.BaseFragment
import com.darja.moviedb.ui.util.ScreenUtil
import com.darja.moviedb.util.DPLog
import org.greenrobot.eventbus.EventBus

class MoviesListFragment: BaseFragment<MoviesListViewModel, MoviesListFragmentView>() {
    override fun getLayoutResId() = R.layout.fragment_movies_list

    override fun createFragmentView() = MoviesListFragmentView()

    override fun getViewModel() = MoviesListViewModel::class.java

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view.onActivityCreated(activity as AppCompatActivity)

        observeViewModel()
        setupViewEvents()

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        activity?.menuInflater?.inflate(R.menu.movies_list, menu)
        view.setupSearchView(menu)
    }

    private fun observeViewModel() {
        DPLog.w("Added observer")
        viewModel.getSearchResult().observe(this, Observer {
            DPLog.i("Movies changed")
            if (it != null && it.isNotEmpty()) {
                view.showMovies(it)
            } else {
                // todo view.showNoMovies(error: String)
                view.showMovies(emptyList())
                view.showEmptyMessage(getString(R.string.error_no_movies_found))
            }
        })

        viewModel.error.observe(this, Observer {
            if (it != null) {
                // todo view.showNoMovies(error: String)
                view.showEmptyMessage(getString(it))
            }
        })

        viewModel.isRequesting.observe(this, Observer { view.setProgressVisibility(it == true) })
    }

    private fun setupViewEvents() {
        view.setMovieClickListener { EventBus.getDefault().post(MovieSelected(it)) }

        view.onSearchQuerySubmitted = {
            viewModel.performSearch(it)
            ScreenUtil.hideSoftKeyboard(activity)
        }

        view.onSearchQueryClosed = viewModel::loadPopular

        view.onRefreshRequested = viewModel::refreshSearch
    }
}
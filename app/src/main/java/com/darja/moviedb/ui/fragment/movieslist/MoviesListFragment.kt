package com.darja.moviedb.ui.fragment.movieslist

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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

    override fun onPause() {
        super.onPause()

        view.onSearchQueryClosed = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        activity?.menuInflater?.inflate(R.menu.movies_list, menu)
        view.setupSearchView(menu)

        if (!TextUtils.isEmpty(viewModel.lastSearchQuery)) {
            view.showSearch(viewModel.lastSearchQuery!!)
            ScreenUtil.hideSoftKeyboard(activity)
        }

        view.onSearchQueryClosed = viewModel::loadPopular
    }

    private fun observeViewModel() {
        viewModel.getSearchResult().observe(this, Observer {
            DPLog.i("Movies changed")
            if (it != null && it.isNotEmpty()) {
                view.showMovies(it)
            } else {
                view.showNoMovies(getString(R.string.error_no_movies_found))
            }
        })

        viewModel.error.observe(this, Observer {
            if (it != null) {
                view.showNoMovies(getString(it))
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

        view.onRefreshRequested = viewModel::refreshSearch
    }
}
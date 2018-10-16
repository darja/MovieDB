package com.darja.moviedb.ui.fragment.movieslist

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import com.darja.moviedb.R
import com.darja.moviedb.ui.MovieSelected
import com.darja.moviedb.ui.fragment.BaseFragment
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        DPLog.checkpoint()
        activity?.menuInflater?.inflate(R.menu.movies_list, menu)
        val myActionMenuItem = menu?.findItem(R.id.action_search)
        val searchView = myActionMenuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
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

    private fun setupViewEvents() {
        view.setMovieClickListener { EventBus.getDefault().post(MovieSelected(it)) }
    }
}
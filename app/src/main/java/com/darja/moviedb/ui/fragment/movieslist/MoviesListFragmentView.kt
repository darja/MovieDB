@file:Suppress("ProtectedInFinal")

package com.darja.moviedb.ui.fragment.movieslist

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import com.darja.moviedb.R
import com.darja.moviedb.db.model.Movie
import com.darja.moviedb.util.DPLog

class MoviesListFragmentView {
    @BindView(R.id.refresh) protected lateinit var refresh: SwipeRefreshLayout
    @BindView(R.id.list) protected lateinit var list: RecyclerView
    @BindView(R.id.empty_message) protected lateinit var emptyMessage: TextView
    @BindView(R.id.toolbar) protected lateinit var toolbar: Toolbar

    private val moviesAdapter = MoviesAdapter()

    var onSearchQuerySubmitted: ((String) -> Unit)? = null
    var onSearchQueryClosed: (() -> (Unit))? = null
    var onRefreshRequested: (() -> (Unit))? = null

    fun onActivityCreated(activity: AppCompatActivity?) {
        list.layoutManager = LinearLayoutManager(activity)
        list.adapter = moviesAdapter

        activity?.setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.popular)

        refresh.setOnRefreshListener {
            DPLog.checkpoint()
            onRefreshRequested?.invoke()
        }
    }

    fun setProgressVisibility(visible: Boolean) {
        refresh.isRefreshing = visible
    }

    private fun showEmptyMessage(message: String) {
        DPLog.vtrace(5, "Empty message: %s", message)
        emptyMessage.text = message
        emptyMessage.visibility = View.VISIBLE
    }

    private fun hideEmptyMessage() {
        emptyMessage.visibility = View.GONE
    }

    fun showMovies(movies: List<Movie>) {
        moviesAdapter.movies = movies
        moviesAdapter.notifyDataSetChanged()
        hideEmptyMessage()
        refresh.isRefreshing = false
    }

    fun showNoMovies(message: String) {
        moviesAdapter.movies = null
        moviesAdapter.notifyDataSetChanged()
        showEmptyMessage(message)
        refresh.isRefreshing = false
    }

    fun setMovieClickListener(listener: ((Movie) -> Any)?) {
        moviesAdapter.clickListener = listener
    }

    fun setupSearchView(menu: Menu) {
        val searchMenuItem = menu.findItem(R.id.action_search) ?: return
        val searchView = searchMenuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return if (onSearchQuerySubmitted != null) {
                    onSearchQuerySubmitted?.invoke(query)
                    true
                } else {
                    false
                }
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        searchMenuItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                DPLog.checkpoint()
                onSearchQueryClosed?.invoke()
                return true
            }
        })
    }
}
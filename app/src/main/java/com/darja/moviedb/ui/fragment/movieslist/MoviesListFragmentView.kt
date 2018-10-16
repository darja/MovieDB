@file:Suppress("ProtectedInFinal")

package com.darja.moviedb.ui.fragment.movieslist

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.darja.moviedb.R
import com.darja.moviedb.db.model.Movie
import com.darja.moviedb.util.DPLog

class MoviesListFragmentView {
    @BindView(R.id.list) protected lateinit var list: RecyclerView
    @BindView(R.id.empty_message) protected lateinit var emptyMessage: TextView
    @BindView(R.id.progress) protected lateinit var progress: View
    @BindView(R.id.toolbar) protected lateinit var toolbar: Toolbar

    private val moviesAdapter = MoviesAdapter()

    var searchQuerySubmitted: ((String) -> Unit)? = null
    var searchQueryClosed: (() -> Unit)? = null

    fun onActivityCreated(activity: AppCompatActivity?) {
        list.layoutManager = LinearLayoutManager(activity)
        list.adapter = moviesAdapter

        activity?.setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.popular)
    }

    fun setProgressVisibility(visible: Boolean?) {
        progress.visibility = if (visible == true) View.VISIBLE else View.GONE
    }

    fun showEmptyMessage(message: String) {
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
    }

    fun setMovieClickListener(listener: ((Movie) -> Any)?) {
        moviesAdapter.clickListener = listener
    }

    fun setupSearchView(menu: Menu) {
        val searchMenuItem = menu.findItem(R.id.action_search) ?: return
        val searchView = searchMenuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return if (searchQuerySubmitted != null) {
                    searchQuerySubmitted?.invoke(query)
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
                searchQueryClosed?.invoke()
                return true
            }
        })
    }
}
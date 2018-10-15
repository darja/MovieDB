@file:Suppress("ProtectedInFinal")

package com.darja.moviedb.ui.fragment.movieslist

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.darja.moviedb.R
import com.darja.moviedb.db.model.Movie

class MoviesListFragmentView {
    @BindView(R.id.list) protected lateinit var list: RecyclerView
    @BindView(R.id.empty_message) protected lateinit var emptyMessage: TextView
    @BindView(R.id.progress) protected lateinit var progress: View

    private val moviesAdapter = MoviesAdapter()

    fun onActivityCreated(activity: Activity?) {
        list.layoutManager = LinearLayoutManager(activity)
        list.adapter = moviesAdapter
    }

    fun setProgressVisibility(visible: Boolean?) {
        progress.visibility = if (visible == true) View.VISIBLE else View.GONE
    }

    fun showEmptyMessage(message: String) {
        emptyMessage.text = message
        emptyMessage.visibility = View.VISIBLE
    }

    fun hideEmptyMessage() {
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
}
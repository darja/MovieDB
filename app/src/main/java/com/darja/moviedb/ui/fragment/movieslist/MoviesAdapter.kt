package com.darja.moviedb.ui.fragment.movieslist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.darja.moviedb.R
import com.darja.moviedb.db.model.Movie
import com.facebook.drawee.view.SimpleDraweeView
import java.text.SimpleDateFormat
import java.util.*

class MoviesAdapter: RecyclerView.Adapter<MovieViewHolder>() {
    var movies: List<Movie>? = null

    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)

        return MovieViewHolder(view)
    }

    override fun getItemCount() = movies?.size ?: 0

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies?.get(position) ?: return

        holder.thumbnail.setImageURI(movie.smallThumbnail)
        holder.title.text = movie.title

        holder.setTextOrHide(holder.genre, movie.genres)
        holder.setTextOrHide(holder.releaseYear,
            if (movie.releaseDate == 0L) null else yearFormat.format(movie.releaseDate))
        holder.setTextOrHide(holder.popularityScore,
            if (movie.popularityScore == 0f) null else String.format("%.0f", movie.popularityScore))
    }
}

class MovieViewHolder(root: View): RecyclerView.ViewHolder(root) {
    init {
        ButterKnife.bind(this, root)
    }

    @BindView(R.id.thumbnail) lateinit var thumbnail: SimpleDraweeView
    @BindView(R.id.title) lateinit var title: TextView
    @BindView(R.id.release_year) lateinit var releaseYear: TextView
    @BindView(R.id.genre) lateinit var genre: TextView
    @BindView(R.id.popularity_score) lateinit var popularityScore: TextView

    fun setTextOrHide(view: TextView, text: String?) {
        if (text != null) {
            view.text = text
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}
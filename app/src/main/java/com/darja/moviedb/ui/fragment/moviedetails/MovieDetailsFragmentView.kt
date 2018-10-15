package com.darja.moviedb.ui.fragment.moviedetails

import android.content.res.Resources
import android.widget.TextView
import butterknife.BindView
import com.darja.moviedb.R
import com.darja.moviedb.db.model.Movie
import com.darja.moviedb.ui.util.setTextOrHide
import com.facebook.drawee.view.SimpleDraweeView
import java.text.SimpleDateFormat
import java.util.*

class MovieDetailsFragmentView {
    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())

    @BindView(R.id.title) lateinit var title: TextView
    @BindView(R.id.thumbnail) lateinit var thumbnail: SimpleDraweeView
    @BindView(R.id.release_year) lateinit var releaseYear: TextView
    @BindView(R.id.runtime) lateinit var runtime: TextView

    fun showMovieDetails(res: Resources, movie: Movie) {
        title.setTextOrHide(movie.title)
        releaseYear.setTextOrHide(if (movie.releaseDate > 0) yearFormat.format(movie.releaseDate) else null)
        runtime.setTextOrHide(
            if (movie.runtime > 0)
                res.getString(R.string.runtime, formatRuntime(movie.runtime))
            else null)
        thumbnail.setImageURI(movie.smallThumbnail)
    }

    private fun formatRuntime(runtime: Int): String {
        val min = runtime % 60
        val hour = runtime / 60

        val sb = StringBuilder()
        if (hour > 0) {
            sb.append(hour)
            sb.append("h")
        }

        if (min > 0) {
            if (sb.isNotEmpty()) {
                sb.append(" ")
            }

            sb.append(min)
            sb.append("m")
        }
        return sb.toString()
    }
}
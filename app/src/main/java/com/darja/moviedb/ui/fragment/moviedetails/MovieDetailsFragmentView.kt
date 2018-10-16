@file:Suppress("ProtectedInFinal")

package com.darja.moviedb.ui.fragment.moviedetails

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import com.darja.moviedb.R
import com.darja.moviedb.db.model.Movie
import com.darja.moviedb.ui.util.setTextOrHide
import com.facebook.drawee.view.SimpleDraweeView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MovieDetailsFragmentView {
    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())

    @BindView(R.id.title) protected lateinit var title: TextView
    @BindView(R.id.thumbnail) protected lateinit var thumbnail: SimpleDraweeView
    @BindView(R.id.release_year) protected lateinit var releaseYear: TextView
    @BindView(R.id.genres) protected lateinit var genres: TextView
    @BindView(R.id.runtime_title) protected lateinit var runtimeTitle: View
    @BindView(R.id.runtime) protected lateinit var runtime: TextView
    @BindView(R.id.revenue_title) protected lateinit var revenueTitle: View
    @BindView(R.id.revenue) protected lateinit var revenue: TextView
    @BindView(R.id.homepage) protected lateinit var homepage: Button
    @BindView(R.id.language_title) protected lateinit var languageTitle: View
    @BindView(R.id.language) protected lateinit var language: TextView
    @BindView(R.id.description) protected lateinit var description: TextView

    fun bindMovieDetails(movie: Movie) {
        title.setTextOrHide(movie.title)
        releaseYear.setTextOrHide(if (movie.releaseDate != null) yearFormat.format(movie.releaseDate) else null)
        runtime.setTextOrHide(formatRuntime(movie.runtime), runtimeTitle)
        revenue.setTextOrHide(formatRevenue(movie.revenue), revenueTitle)
        description.setTextOrHide(movie.description)
        genres.setTextOrHide(movie.genres)
        language.setTextOrHide(movie.language, languageTitle)
        thumbnail.setImageURI(movie.smallThumbnail)

        homepage.visibility = if (TextUtils.isEmpty(movie.homepage)) View.GONE else View.VISIBLE
    }

    fun showError(context: Context?, error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    fun setHomepageClickListener(listener: View.OnClickListener) {
        homepage.setOnClickListener(listener)
    }

    private fun formatRuntime(runtime: Int?): String? {
        if (runtime == null || runtime == 0) {
            return null
        }

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

    private fun formatRevenue(revenue: Int?): String? {
        if (revenue == null || revenue == 0) {
            return null
        }

        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        formatter.maximumFractionDigits = 0
        return formatter.format(revenue)
    }
}
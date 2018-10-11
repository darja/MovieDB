package com.darja.moviedb.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.darja.moviedb.R
import com.darja.moviedb.api.TmdbApi
import com.darja.moviedb.api.model.ApiGenresList
import com.darja.moviedb.api.model.ApiMoviesPage
import com.darja.moviedb.db.dao.GenreDao
import com.darja.moviedb.db.dao.MovieDao
import com.darja.moviedb.db.model.Genre
import com.darja.moviedb.db.model.Movie
import com.darja.moviedb.util.DPLog
import dagger.android.AndroidInjection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    internal lateinit var api: TmdbApi

    @Inject
    internal lateinit var genreDao: GenreDao

    @Inject
    internal lateinit var movieDao: MovieDao

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        api.getGenres().enqueue(object: Callback<ApiGenresList> {
            override fun onFailure(call: Call<ApiGenresList>, t: Throwable) {
                DPLog.e("Failure")
            }

            override fun onResponse(call: Call<ApiGenresList>, response: Response<ApiGenresList>) {
                response.body()
                    ?.forEach{
                        genreDao.upsert(Genre(it))
                    }
            }
        })

        api.getPopularMovies().enqueue(object: Callback<ApiMoviesPage> {
            override fun onResponse(call: Call<ApiMoviesPage>, response: Response<ApiMoviesPage>) {
                response.body()?.movies
                    ?.forEach{
                        movieDao.upsert(Movie(it))
                    }
            }

            override fun onFailure(call: Call<ApiMoviesPage>, t: Throwable) {
                DPLog.e("Cannot request popular")
            }

        })
    }
}

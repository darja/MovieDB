package com.darja.moviedb.activity

import android.os.Bundle
import com.darja.moviedb.R
import com.darja.moviedb.api.TmdbApi
import com.darja.moviedb.api.model.ApiGenresList
import com.darja.moviedb.db.dao.GenreDao
import com.darja.moviedb.db.dao.MovieDao
import com.darja.moviedb.db.dao.MovieSearchDao
import com.darja.moviedb.db.dao.MovieSearchItemDao
import com.darja.moviedb.db.model.Genre
import com.darja.moviedb.ui.MovieSelected
import com.darja.moviedb.ui.fragment.moviedetails.MovieDetailsFragment
import com.darja.moviedb.ui.fragment.movieslist.MoviesListFragment
import com.darja.moviedb.util.DPLog
import dagger.android.AndroidInjection
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    internal lateinit var api: TmdbApi

    @Inject internal lateinit var genreDao: GenreDao
    @Inject internal lateinit var movieDao: MovieDao
    @Inject internal lateinit var searchDao: MovieSearchDao
    @Inject internal lateinit var searchContentDao: MovieSearchItemDao

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // todo check search.updatedAll
        // todo cascade delete
        searchContentDao.deleteAll()
        searchDao.deleteAll()

        api.getGenres().enqueue(object: Callback<ApiGenresList> {
            override fun onFailure(call: Call<ApiGenresList>, t: Throwable) {
                DPLog.e("Failure")
            }

            override fun onResponse(call: Call<ApiGenresList>, response: Response<ApiGenresList>) {
                response.body()
                    ?.forEach{
                        genreDao.upsert(Genre(it))
                        showSearchResult()
                    }
            }
        })
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun showSearchResult() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MoviesListFragment())
            .commit()
    }

    @Suppress("ProtectedInFinal")
    @Subscribe
    protected fun onMovieSelected(event: MovieSelected) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MovieDetailsFragment.newInstance(event.movie.movieId))
            .addToBackStack(null)
            .commit()
    }
}

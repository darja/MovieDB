package com.darja.moviedb.activity.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.darja.moviedb.R
import com.darja.moviedb.api.TmdbApi
import com.darja.moviedb.api.model.ApiGenresList
import com.darja.moviedb.db.dao.GenreDao
import com.darja.moviedb.db.dao.MovieDao
import com.darja.moviedb.db.dao.MovieSearchDao
import com.darja.moviedb.db.dao.MovieSearchItemDao
import com.darja.moviedb.db.model.Genre
import com.darja.moviedb.util.DPLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Suppress("ProtectedInFinal")
class MainViewModel @Inject constructor(): ViewModel() {
    @Inject protected lateinit var api: TmdbApi

    @Inject protected lateinit var genreDao: GenreDao
    @Inject protected lateinit var movieDao: MovieDao
    @Inject protected lateinit var searchDao: MovieSearchDao
    @Inject protected lateinit var searchContentDao: MovieSearchItemDao

    val initialized = MutableLiveData<Boolean>()
    val error = MutableLiveData<Int>()

    companion object {
        val SEARCH_LIFETIME = TimeUnit.HOURS.toMillis(1)
    }

    fun clearOutdated() {
        val searchDeadline = System.currentTimeMillis() - SEARCH_LIFETIME

        searchDao.deleteOlderThan(searchDeadline)
        val searchContent = searchContentDao.select()
        DPLog.i("Search content table: %s items", searchContent.size)
    }

    fun updateGenres() {
        api.getGenres().enqueue(object: Callback<ApiGenresList> {
            override fun onFailure(call: Call<ApiGenresList>, t: Throwable) {
                error.postValue(R.string.error_cannot_reach_server)
            }

            override fun onResponse(call: Call<ApiGenresList>, response: Response<ApiGenresList>) {
                val genresList = response.body()
                if (genresList != null && !genresList.isEmpty()) {
                    genreDao.clear()
                    genresList.forEach{
                        genreDao.upsert(Genre(it))
                    }
                } else {
                    error.postValue(R.string.error_cannot_reach_server)
                }
                initialized.postValue(true)
            }
        })
    }
}
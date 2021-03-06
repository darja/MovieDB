package com.darja.moviedb.ui.fragment.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.darja.moviedb.api.TmdbApi
import com.darja.moviedb.api.model.ApiMovie
import com.darja.moviedb.db.dao.MovieDao
import com.darja.moviedb.db.model.Movie
import com.darja.moviedb.util.DPLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(): ViewModel() {
    @Inject lateinit var movieDao: MovieDao
    @Inject lateinit var api: TmdbApi

    private var movieLiveData = MutableLiveData<Movie>()
    val movieLoadedLiveData = MutableLiveData<Boolean>()

    var movieId: Long = 0

    fun getMovie(): LiveData<Movie> {
        val movie = movieDao.select(movieId)

        if (movieLiveData.value == null) {
            movieLiveData.postValue(movie)
        }

        if (movie.needsMoreDetails()) {
            requestMovieDetails()
        }

        return movieLiveData
    }

    private fun requestMovieDetails() {
        val call = api.getMovieDetails(movieId)
        call.enqueue(object: Callback<ApiMovie> {
            override fun onFailure(call: Call<ApiMovie>, t: Throwable) {
                DPLog.e(t)
                movieLoadedLiveData.postValue(false)
            }

            override fun onResponse(call: Call<ApiMovie>, response: Response<ApiMovie>) {
                val body = response.body()
                if (body != null) {
                    val movie = Movie(body)
                    movieDao.upsert(movie)
                    movieLiveData.postValue(movie)
                    movieLoadedLiveData.postValue(true)
                } else {
                    movieLoadedLiveData.postValue(false)
                }
            }
        })
    }
}
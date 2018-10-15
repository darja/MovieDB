package com.darja.moviedb.ui.fragment.moviedetails

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.darja.moviedb.db.dao.MovieDao
import com.darja.moviedb.db.model.Movie
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(): ViewModel() {
    @Inject lateinit var movieDao: MovieDao

    private var movie = MutableLiveData<Movie>()

    var movieId: Long = 0

    fun getMovie(): LiveData<Movie> {
        if (movie.value == null) {
            movie.postValue(movieDao.select(movieId))
        }

        // todo if not all fields are fulfilled, run API details request
        return movie
    }
}
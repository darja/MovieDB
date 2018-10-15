package com.darja.moviedb.ui.fragment.movieslist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.darja.moviedb.R
import com.darja.moviedb.api.TmdbApi
import com.darja.moviedb.api.model.ApiMoviesPage
import com.darja.moviedb.db.dao.GenreDao
import com.darja.moviedb.db.dao.MovieDao
import com.darja.moviedb.db.dao.MovieSearchDao
import com.darja.moviedb.db.dao.MovieSearchItemDao
import com.darja.moviedb.db.model.Movie
import com.darja.moviedb.db.model.MovieSearch
import com.darja.moviedb.db.model.MovieSearchItem
import com.darja.moviedb.util.DPLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MoviesListViewModel @Inject constructor(): ViewModel() {
    @Inject lateinit var api: TmdbApi

    @Inject lateinit var genreDao: GenreDao
    @Inject lateinit var movieDao: MovieDao
    @Inject lateinit var searchDao: MovieSearchDao
    @Inject lateinit var searchContentDao: MovieSearchItemDao

    var apiCall: Call<*>? = null

    val error = MutableLiveData<Int>()
    val isRequesting = MutableLiveData<Boolean>()

    private val searchResult = MutableLiveData<List<Movie>>()

    override fun onCleared() {
        super.onCleared()

        try {
            if (apiCall != null) {
                apiCall?.cancel()
                DPLog.w("API call cancelled")
            }
        } catch (ignored: Exception) {}
    }

    internal fun getSearchResult(): LiveData<List<Movie>> {
        if (searchResult.value == null) {
            searchPopular()
        }
        return searchResult
    }

    private fun searchPopular() {
        val call = api.getPopularMovies()
        apiCall = call
        call
            .enqueue(object: Callback<ApiMoviesPage> {
                override fun onFailure(call: Call<ApiMoviesPage>, t: Throwable) {
                    apiCall = null
                    error.postValue(R.string.error_cannot_reach_server)
                    isRequesting.postValue(false)
                }

                override fun onResponse(call: Call<ApiMoviesPage>, response: Response<ApiMoviesPage>) {
                    apiCall = null
                    val body = response.body()

                    if (body != null) {
                        val searchId = saveSearchInDb(body)
                        searchResult.postValue(movieDao.getSearchContent(searchId))
                    }
                    isRequesting.postValue(false)
                }
            })
    }

    /**
     * @return search result id
     */
    private fun saveSearchInDb(response: ApiMoviesPage): Long {
        val search = MovieSearch.createPopular()
        search.page = response.page
        search.totalPagesCount = response.totalPagesCount
        search.updatedAt = System.currentTimeMillis()
        val searchId = searchDao.upsert(search)

        response.movies
            .forEach{ apiMovie ->
                val genreIds = apiMovie.genreIds

                // save to movies
                val movie = Movie(apiMovie)
                movie.genres = if (genreIds != null)
                        genreDao.select(genreIds).joinToString { it.title }
                    else null
                movieDao.upsert(movie)

                // link to movies list
                searchContentDao.insert(MovieSearchItem(searchId, movie.movieId))
            }

        return searchId
    }
}
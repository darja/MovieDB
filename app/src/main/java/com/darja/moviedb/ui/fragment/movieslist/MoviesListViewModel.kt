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
            loadPopular()
        }
        return searchResult
    }

    internal fun performSearch(query: String) {
        DPLog.checkpoint()
        loadSearchResult(query, null) { api.searchMovies(query) }
    }

    private fun loadPopular() {
        loadSearchResult(null, MovieSearch.CATEGORY_POPULAR) { api.getPopularMovies() }
    }

    private fun loadSearchResult(query: String?, category: String?, createCall: () -> Call<ApiMoviesPage>) {
        val cached = searchDao.select(query, category)
        // todo check expired?
        if (cached != null) {
            DPLog.i("%s is cached", if (query == null) "Query[$query]" else "Category[$category]" )
            showCachedSearchResult(cached.rowId)
            isRequesting.postValue(false)
            return
        }

        val call = createCall.invoke()
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
                        val searchId = saveSearchInDb(query, category, body)
                        showCachedSearchResult(searchId)
                    }
                    isRequesting.postValue(false)
                }
            })
    }

    private fun showCachedSearchResult(searchId: Long) {
        searchResult.postValue(movieDao.getSearchContent(searchId))
    }

    /**
     * @return search result id
     */
    private fun saveSearchInDb(query: String?, category: String?, response: ApiMoviesPage): Long {
        val search = MovieSearch(query, category, response.page, response.totalPagesCount)
        val searchId = searchDao.insert(search)

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
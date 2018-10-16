package com.darja.moviedb.ui.fragment.movieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

@Suppress("ProtectedInFinal")
class MoviesListViewModel @Inject constructor(): ViewModel() {
    @Inject protected lateinit var api: TmdbApi

    @Inject protected lateinit var genreDao: GenreDao
    @Inject protected lateinit var movieDao: MovieDao
    @Inject protected lateinit var searchDao: MovieSearchDao
    @Inject protected lateinit var searchContentDao: MovieSearchItemDao

    private var apiCall: Call<*>? = null

    private var lastSearchId: Long? = null

    val error = MutableLiveData<Int>()
    val isRequesting = MutableLiveData<Boolean>()
    var lastSearchQuery: String? = null
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

    internal fun refreshSearch() {
        val searchId = lastSearchId

        if (searchId != null) {
            val lastSearch = searchDao.select(searchId)
            if (lastSearch != null) {
                searchDao.clearSearch(searchId)

                if (lastSearch.query != null) {
                    performSearch(lastSearch.query!!)

                } else if (lastSearch.category == MovieSearch.CATEGORY_POPULAR) {
                    loadPopular()
                }
            }
        }
    }

    internal fun getSearchResult(): LiveData<List<Movie>> {
        if (searchResult.value == null) {
            loadPopular()
        }
        return searchResult
    }

    internal fun performSearch(query: String) {
        loadSearchResult(query, null) { api.searchMovies(query) }
    }

    internal fun loadPopular() {
        loadSearchResult(null, MovieSearch.CATEGORY_POPULAR) { api.getPopularMovies() }
    }

    private fun loadSearchResult(query: String?, category: String?, createCall: () -> Call<ApiMoviesPage>) {
        DPLog.itrace(3, "Load query [%s], category [%s]", query, category)
        val cached = searchDao.select(query, category)
        // todo check expired?
        if (cached != null) {
            DPLog.i("%s is cached", if (query != null) "Query[$query]" else "Category[$category]" )
            showCachedSearchResult(cached.rowId)
            isRequesting.postValue(false)
            return
        }

        apiCall?.cancel()

        val call = createCall.invoke()
        apiCall = call
        call
            .enqueue(object: Callback<ApiMoviesPage> {
                override fun onFailure(call: Call<ApiMoviesPage>, t: Throwable) {
                    if (!call.isCanceled) {
                        apiCall = null
                        error.postValue(R.string.error_cannot_reach_server)
                        isRequesting.postValue(false)
                    }
                }

                override fun onResponse(call: Call<ApiMoviesPage>, response: Response<ApiMoviesPage>) {
                    apiCall = null
                    val body = response.body()

                    if (body != null) {
                        val searchId = saveSearchInDb(query, category, body)
                        showCachedSearchResult(searchId)
                    } else {
                        error.postValue(R.string.error_cannot_reach_server)
                    }
                    isRequesting.postValue(false)
                    lastSearchQuery = query
                }
            })
    }

    private fun showCachedSearchResult(searchId: Long) {
        lastSearchId = searchId
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
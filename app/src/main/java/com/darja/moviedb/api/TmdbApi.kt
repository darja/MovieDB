package com.darja.moviedb.api

import com.darja.moviedb.api.model.ApiGenresList
import com.darja.moviedb.api.model.ApiMovie
import com.darja.moviedb.api.model.ApiMoviesPage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("movie/{movieId}")
    fun getMovieDetails(@Path("movieId") movieId: Long): Call<ApiMovie>

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int = 1): Call<ApiMoviesPage>

    @GET("search/movie")
    fun searchMovies(@Query("query") query: String, @Query("page") page: Int = 1): Call<ApiMoviesPage>

    @GET("genre/movie/list")
    fun getGenres(): Call<ApiGenresList>
}
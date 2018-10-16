package com.darja.moviedb.di

import android.app.Application
import com.darja.moviedb.R
import com.darja.moviedb.api.ApiKeyInterceptor
import com.darja.moviedb.api.TmdbApi
import com.darja.moviedb.api.model.ApiGenresArrayDeserializer
import com.darja.moviedb.api.model.ApiGenresList
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): TmdbApi = retrofit.create(TmdbApi::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(app: Application): Retrofit {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .registerTypeAdapter(ApiGenresList::class.java, ApiGenresArrayDeserializer())
            .create()

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ApiKeyInterceptor(app.getString(R.string.tmdp_api_key)))
            .build()


        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
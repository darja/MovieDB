package com.darja.moviedb.di

import android.app.Application
import android.content.Context
import com.darja.moviedb.MovieDbApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {
    @Provides
    @Singleton
    fun providesApp() = app

    @Provides
    @Singleton
    fun providesAppContext(app: MovieDbApp): Context = app.applicationContext
}
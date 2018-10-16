package com.darja.moviedb.di

import android.app.Application
import androidx.room.Room
import android.content.Context
import com.darja.moviedb.MovieDbApp
import com.darja.moviedb.db.MoviesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
class AppModule(private val app: Application) {
    @Provides
    @Singleton
    fun providesApp() = app

    @Provides
    @Singleton
    fun providesAppContext(app: MovieDbApp): Context = app.applicationContext

    @Provides
    @Singleton
    fun providesAppDatabase(): MoviesDatabase {
        return Room
            .databaseBuilder(app.applicationContext,
                MoviesDatabase::class.java,
                MoviesDatabase.DB_NAME)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun providesGenreDao(db: MoviesDatabase) = db.genreDao()

    @Provides
    @Singleton
    fun providesMovieDao(db: MoviesDatabase) = db.movieDao()

    @Provides
    @Singleton
    fun providesSearchDao(db: MoviesDatabase) = db.searchDao()

    @Provides
    @Singleton
    fun providesSearchContentDao(db: MoviesDatabase) = db.searchContentDao()
}
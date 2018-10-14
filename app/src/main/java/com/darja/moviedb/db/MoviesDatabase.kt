package com.darja.moviedb.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.darja.moviedb.db.dao.GenreDao
import com.darja.moviedb.db.dao.MovieDao
import com.darja.moviedb.db.dao.MovieSearchDao
import com.darja.moviedb.db.dao.MovieSearchItemDao
import com.darja.moviedb.db.model.Genre
import com.darja.moviedb.db.model.Movie
import com.darja.moviedb.db.model.MovieSearch
import com.darja.moviedb.db.model.MovieSearchItem

@Database(entities = [
    (Genre::class),
    (Movie::class),
    (MovieSearch::class),
    (MovieSearchItem::class)
], version = 1)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun movieDao(): MovieDao
    abstract fun searchDao(): MovieSearchDao
    abstract fun searchContentDao(): MovieSearchItemDao

    companion object {
        const val DB_NAME = "moviesdb"
    }
}
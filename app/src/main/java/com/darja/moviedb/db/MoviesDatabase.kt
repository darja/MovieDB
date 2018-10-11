package com.darja.moviedb.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.darja.moviedb.db.dao.GenreDao
import com.darja.moviedb.db.dao.MovieDao
import com.darja.moviedb.db.model.Genre
import com.darja.moviedb.db.model.Movie

@Database(entities = [(Genre::class), (Movie::class)], version = 1)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun movieDao(): MovieDao

    companion object {
        const val DB_NAME = "moviesdb"
    }
}
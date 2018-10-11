package com.darja.moviedb.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.darja.moviedb.db.dao.GenreDao
import com.darja.moviedb.db.model.Genre

@Database(entities = [(Genre::class)], version = 1)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun genreDao(): GenreDao

    companion object {
        const val DB_NAME = "moviesdb"
    }
}
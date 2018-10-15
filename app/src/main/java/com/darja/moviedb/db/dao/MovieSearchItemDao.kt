package com.darja.moviedb.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.darja.moviedb.db.model.MovieSearchItem

@Dao
interface MovieSearchItemDao {
    @Insert
    fun insert(item: MovieSearchItem)

    @Query("select * from movie_search_content")
    fun select(): List<MovieSearchItem>
}
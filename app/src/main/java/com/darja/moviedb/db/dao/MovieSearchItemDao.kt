package com.darja.moviedb.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.darja.moviedb.db.model.MovieSearchItem

@Dao
interface MovieSearchItemDao {
    @Insert
    fun insert(item: MovieSearchItem)

    @Query("select * from movie_search_content")
    fun select(): List<MovieSearchItem>
}
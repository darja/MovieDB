package com.darja.moviedb.db.dao

import android.arch.persistence.room.*
import com.darja.moviedb.db.model.MovieSearch

@Dao
interface MovieSearchDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: MovieSearch): Long

    @Update
    fun update(item: MovieSearch)

    @Query("delete from movies_search where updatedAt < :date")
    fun deleteOlderThan(date: Long)

    @Query("select * from movies_search where `query`=:query or category=:category")
    fun select(query: String?, category: String?): MovieSearch?
}
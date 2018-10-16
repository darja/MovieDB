package com.darja.moviedb.db.dao

import androidx.room.*
import com.darja.moviedb.db.model.MovieSearch

@Dao
interface MovieSearchDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: MovieSearch): Long

    @Update
    fun update(item: MovieSearch)

    @Query("delete from movies_search where updatedAt < :date")
    fun deleteOlderThan(date: Long)

    @Query("delete from movies_search where `rowId`=:id")
    fun clearSearch(id: Long)

    @Query("select * from movies_search where `query`=:query or category=:category")
    fun select(query: String?, category: String?): MovieSearch?

    @Query("select * from movies_search where `rowId`=:id")
    fun select(id: Long): MovieSearch?
}
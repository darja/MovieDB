package com.darja.moviedb.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.darja.moviedb.db.model.MovieSearch

@Dao
abstract class MovieSearchDao: DaoWithUpsert<MovieSearch>() {
    @Query("select `rowId` from movies_search where `query`=:query or category=:category")
    abstract fun select(query: String?, category: String?): Long

    @Query("delete from movies_search")
    abstract fun deleteAll()

    override fun select(item: MovieSearch) = select(item.query, item.category)
}
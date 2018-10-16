package com.darja.moviedb.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.darja.moviedb.db.model.Movie

@Dao
abstract class MovieDao: DaoWithUpsert<Movie>() {
    @Query("""
        select m.* from movies m
        left join movie_search_content mc on m.movieId = mc.movieId
        where mc.searchId=:searchId
        order by m.popularityScore desc
        """)
    abstract fun getSearchContent(searchId: Long): List<Movie>

    @Query("select `rowId` from movies where movieId = :movieId limit 1")
    abstract fun selectRowId(movieId: Long): Long

    override fun select(item: Movie) = selectRowId(item.movieId)

    @Query("select * from movies where movieId = :movieId limit 1")
    abstract fun select(movieId: Long): Movie
}
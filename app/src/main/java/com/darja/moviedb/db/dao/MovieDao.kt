package com.darja.moviedb.db.dao

import android.arch.persistence.room.*
import com.darja.moviedb.db.model.Movie
import com.darja.moviedb.util.DPLog

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: Movie): Long

    @Update
    fun update(item: Movie)

    @Query("""
        select m.* from movies m
        left join movie_search_content mc on m.movieId = mc.movieId
        where mc.searchId=:searchId
        order by m.popularityScore desc
        """)
    fun getSearchContent(searchId: Long): List<Movie>

    @Query("select `rowId` from movies where movieId = :movieId limit 1")
    fun selectRowId(movieId: Long): Long

    @Query("select * from movies where movieId = :movieId limit 1")
    fun select(movieId: Long): Movie

    @Transaction
    fun upsert(item: Movie): Long {
        var id = insert(item)
        if (id < 0) {
            val cachedMovie = select(item.movieId)
            cachedMovie.append(item)
            update(cachedMovie)
            id = cachedMovie.rowId
            DPLog.v("Updated %s: %s", id, item)
        } else {
            DPLog.v("Inserted %s: %s", id, item)
        }
        return id
    }
}
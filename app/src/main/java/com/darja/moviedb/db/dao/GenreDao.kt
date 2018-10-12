package com.darja.moviedb.db.dao

import android.arch.persistence.room.*
import com.darja.moviedb.db.model.Genre
import com.darja.moviedb.util.DPLog

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: Genre): Long

    @Update
    fun update(item: Genre)

    @Query("select * from genres")
    fun select(): Array<Genre>

    @Query("select * from genres where genreId in(:ids)")
    fun select(ids: Array<Int>): Array<Genre>

    @Transaction
    fun upsert(item: Genre) {
        val id = insert(item)
        if (id < 0) {
            update(item)
            DPLog.w("Genre updated: %s", item.title)
        } else {
            DPLog.d("Genre inserted: %s", item.title)
        }
    }
}
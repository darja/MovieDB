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

    @Transaction
    fun upsert(item: Movie) {
        val id = insert(item)
        if (id < 0) {
            DPLog.w("Movie updated: %s", item.title)
            update(item)
        } else {
            DPLog.d("Movie inserted: %s", item.title)
        }
    }
}
package com.darja.moviedb.db.dao

import androidx.room.*
import com.darja.moviedb.db.model.Genre
import com.darja.moviedb.util.DPLog

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: Genre): Long

    @Update
    fun update(item: Genre)

    @Query("select `rowId` from genres where genreId = :genreId")
    fun select(genreId: Int): Long

    @Query("select * from genres")
    fun select(): Array<Genre>

    @Query("select * from genres where genreId in(:ids)")
    fun select(ids: Array<Int>): Array<Genre>

    @Query("delete from genres")
    fun clear()

    @Transaction
    fun upsert(item: Genre): Long {
        var id = insert(item)
        if (id < 0) {
            update(item)
            id = select(item.genreId)
            DPLog.v("Updated %s: %s", id, item)
        } else {
            DPLog.v("Inserted %s: %s", id, item)
        }
        return id
    }
}
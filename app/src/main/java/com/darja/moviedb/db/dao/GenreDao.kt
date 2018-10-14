package com.darja.moviedb.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.darja.moviedb.db.model.Genre

@Dao
abstract class GenreDao: DaoWithUpsert<Genre>() {
    @Query("select * from genres")
    abstract fun select(): Array<Genre>

    @Query("select * from genres where genreId in(:ids)")
    abstract fun select(ids: Array<Int>): Array<Genre>

    @Query("select `rowId` from genres where genreId = :genreId")
    abstract fun select(genreId: Int): Long

    override fun select(item: Genre) = select(item.genreId)
}
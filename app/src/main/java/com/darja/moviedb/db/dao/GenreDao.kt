package com.darja.moviedb.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import com.darja.moviedb.db.model.Genre

@Dao
interface GenreDao {
    @Insert
    fun insert(item: Genre): Long
}
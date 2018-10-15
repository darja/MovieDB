package com.darja.moviedb.db.dao

import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Transaction
import android.arch.persistence.room.Update
import com.darja.moviedb.util.DPLog

abstract class DaoWithUpsert<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(item: T): Long

    @Update
    abstract fun update(item: T)

    abstract fun select(item:T): Long

    @Transaction
    open fun upsert(item: T): Long {
        var id = insert(item)
        if (id < 0) {
            update(item)
            id = select(item)
            DPLog.v("Updated %s: %s", id, item)
        } else {
            DPLog.v("Inserted %s: %s", id, item)
        }
        return id
    }
}
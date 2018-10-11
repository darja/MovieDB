package com.darja.moviedb.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.darja.moviedb.api.model.ApiGenre

@Entity(tableName = "genres")
class Genre() {
    @PrimaryKey(autoGenerate = true) var rowId: Long = 0
    @ColumnInfo var genreId: Int = 0
    @ColumnInfo lateinit var title: String

    constructor(src: ApiGenre): this() {
        genreId = src.id
        title = src.title
    }
}
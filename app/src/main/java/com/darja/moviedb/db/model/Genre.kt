package com.darja.moviedb.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.darja.moviedb.api.model.ApiGenre

@Entity(tableName = "genres",
    indices = [Index("genreId", unique = true)])
class Genre() {
    @PrimaryKey(autoGenerate = true) var rowId: Long = 0
    @ColumnInfo var genreId: Int = 0
    @ColumnInfo lateinit var title: String

    constructor(src: ApiGenre): this() {
        genreId = src.id
        title = src.title
    }

    override fun toString() = "Genre[$genreId:\t$title]"
}
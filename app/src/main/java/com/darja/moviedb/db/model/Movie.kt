package com.darja.moviedb.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.darja.moviedb.api.model.ApiMovie

@Entity(tableName = "movies",
    indices = [Index("movieId", unique = true)]
)
class Movie() {
    @PrimaryKey(autoGenerate = true) var rowId: Long = 0
    @ColumnInfo
    var movieId: Long = 0

    @ColumnInfo var title: String? = null
    @ColumnInfo var thumbnail: String? = null
    @ColumnInfo var releaseDate: Long = 0
    @ColumnInfo var popularityScore: Float = 0f
    @ColumnInfo var description: String? = null
    @ColumnInfo var revenue: Int? = null
    @ColumnInfo var runtime: Int? = null
    @ColumnInfo var homepage: String? = null
    @ColumnInfo var genres: String? = null

    constructor(src: ApiMovie): this() {
        append(src)
    }

    fun append(src: ApiMovie) {
        movieId = src.id
        title = src.title
        thumbnail = src.thumbnail
        releaseDate = src.releaseDate?.time ?: 0
        popularityScore = src.popularityScore
        description = src.description
        revenue = src.revenue
        runtime = src.runtime
        homepage = src.homepage
        genres = src.genres?.joinToString { it.title }
    }
}
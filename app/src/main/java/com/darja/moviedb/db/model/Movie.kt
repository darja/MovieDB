package com.darja.moviedb.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.text.TextUtils
import com.darja.moviedb.api.model.ApiMovie
import java.util.*

@Entity(tableName = "movies",
    indices = [Index("movieId", unique = true)]
)
class Movie() {
    @PrimaryKey(autoGenerate = true) var rowId: Long = 0
    @ColumnInfo
    var movieId: Long = 0

    @ColumnInfo var title: String? = null
    @ColumnInfo var relativeThumbnailPath: String? = null
    @ColumnInfo var releaseDate: Long? = null
    @ColumnInfo var popularityScore: Float? = null
    @ColumnInfo var description: String? = null
    @ColumnInfo var revenue: Int? = null
    @ColumnInfo var runtime: Int? = null
    @ColumnInfo var homepage: String? = null
    @ColumnInfo var genres: String? = null
    @ColumnInfo var language: String? = null

    val smallThumbnail: String
        get() =
            if (!TextUtils.isEmpty(relativeThumbnailPath))
                "https://image.tmdb.org/t/p/w200$relativeThumbnailPath"
            else ""

    constructor(src: ApiMovie): this() {
        movieId = src.id
        title = src.title
        relativeThumbnailPath = src.thumbnail
        releaseDate = src.releaseDate?.time
        popularityScore = src.popularityScore
        description = src.description
        revenue = src.revenue
        runtime = src.runtime
        homepage = src.homepage
        genres = src.genres?.joinToString { it.title }

        if (src.language != null) {
            language = Locale(src.language).displayLanguage
        }
    }

    fun append(src: Movie) {
        title = src.title ?: title
        relativeThumbnailPath = src.relativeThumbnailPath ?: relativeThumbnailPath
        releaseDate = src.releaseDate ?: releaseDate
        popularityScore = src.popularityScore ?: popularityScore
        description = src.description ?: description
        revenue = src.revenue ?: revenue
        runtime = src.runtime ?: runtime
        homepage = src.homepage ?: homepage
        genres = src.genres ?: genres
        language = src.language ?: language
    }

    fun needsMoreDetails() = arrayOf(revenue, homepage, runtime).all { it == null }

    override fun toString() = "Movie[$movieId:$title]"

}
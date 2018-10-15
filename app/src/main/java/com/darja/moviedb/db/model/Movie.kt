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
    @ColumnInfo var releaseDate: Long = 0
    @ColumnInfo var popularityScore: Float = 0f
    @ColumnInfo var description: String? = null
    @ColumnInfo var revenue: Int? = null
    @ColumnInfo var runtime: Int = 0
    @ColumnInfo var homepage: String? = null
    @ColumnInfo var genres: String? = null
    @ColumnInfo var language: String? = null

    val smallThumbnail: String
        get() =
            if (!TextUtils.isEmpty(relativeThumbnailPath))
                "https://image.tmdb.org/t/p/w200$relativeThumbnailPath"
            else ""

    constructor(src: ApiMovie): this() {
        append(src)
    }

    fun append(src: ApiMovie) {
        movieId = src.id
        title = src.title
        relativeThumbnailPath = src.thumbnail
        releaseDate = src.releaseDate?.time ?: 0
        popularityScore = src.popularityScore
        description = src.description
        revenue = src.revenue
        runtime = src.runtime ?: 0
        homepage = src.homepage
        genres = src.genres?.joinToString { it.title }

        if (src.language != null) {
            language = Locale(src.language).displayLanguage
        }
    }

    fun needsMoreDetails() = arrayOf(revenue, homepage).all { it == null } && runtime == 0

    override fun toString() = "Movie[$movieId:$title]"

}
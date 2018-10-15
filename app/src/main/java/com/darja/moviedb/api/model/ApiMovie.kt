package com.darja.moviedb.api.model

import com.google.gson.annotations.SerializedName
import java.util.*

class ApiMovie {
    @SerializedName("id")
    var id: Long = 0

    @SerializedName("title")
    lateinit var title: String

    @SerializedName("genre_ids")
    var genreIds: Array<Int>? = null

    @SerializedName("genres")
    var genres: Array<ApiGenre>? = null

    @SerializedName("poster_path")
    var thumbnail: String? = null

    @SerializedName("release_date")
    var releaseDate: Date? = null

    @SerializedName("popularity")
    var popularityScore: Float = 0f

    @SerializedName("overview")
    var description: String? = null

    @SerializedName("revenue")
    var revenue: Int? = null

    @SerializedName("runtime")
    var runtime: Int? = null

    @SerializedName("homepage")
    var homepage: String? = null
}
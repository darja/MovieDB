package com.darja.moviedb.api.model

import com.google.gson.annotations.SerializedName

class ApiMovie {
    @SerializedName("title")
    lateinit var title: String

    @SerializedName("genre_ids")
    var genreIds: Array<String>? = null
    
    @SerializedName("poster_path")
    var thumbnail: String? = null

    // todo add date parser
//    @SerializedName("release_date")
//    var releaseDate: String?

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
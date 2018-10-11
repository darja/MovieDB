package com.darja.moviedb.api.model

import com.google.gson.annotations.SerializedName

class ApiGenre {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    lateinit var title: String
}
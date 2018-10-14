package com.darja.moviedb.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey

@Entity(tableName = "movie_search_content",
    primaryKeys = [ ("searchId"), ("movieId")],
    foreignKeys = [
        (ForeignKey(entity = MovieSearch::class,
            parentColumns = [("rowId")],
            childColumns = [("searchId")])),
        (ForeignKey(entity = Movie::class,
            parentColumns = [("movieId")],
            childColumns = [("movieId")]))
    ]
)
class MovieSearchItem() {
    var searchId: Long = 0
    var movieId: Long = 0

    constructor(searchId: Long, movieId: Long): this() {
        this.searchId = searchId
        this.movieId = movieId
    }
}
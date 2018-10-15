package com.darja.moviedb.db.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index

@Entity(tableName = "movie_search_content",
    primaryKeys = [ ("searchId"), ("movieId")],
    indices = [
        (Index("searchId", unique = false)),
        (Index("movieId", unique = false))],
    foreignKeys = [
        (ForeignKey(entity = MovieSearch::class,
            parentColumns = [("rowId")],
            childColumns = [("searchId")],
            onDelete = ForeignKey.CASCADE)),
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
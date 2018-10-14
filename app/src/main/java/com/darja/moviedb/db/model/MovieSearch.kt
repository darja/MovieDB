package com.darja.moviedb.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "movies_search",
    indices = [
        (Index("query", unique = true)),
        (Index("category", unique = true))
    ])
class MovieSearch() {
    @PrimaryKey(autoGenerate = true) var rowId: Long = 0
    /** Optional search query string */
    @ColumnInfo var query: String? = null
    /** Optional search category (e.g popular)*/
    @ColumnInfo var category: String? = null
    @ColumnInfo var page: Int = 1
    @ColumnInfo var totalPagesCount: Int = 1
    @ColumnInfo var updatedAt: Long = System.currentTimeMillis()

    companion object {
        fun createPopular(): MovieSearch {
            val search = MovieSearch()
            search.category = "popular"
            return search
        }
    }

    override fun toString() = "MovieSearch: query[$query], category[$category], page[$page]"
}
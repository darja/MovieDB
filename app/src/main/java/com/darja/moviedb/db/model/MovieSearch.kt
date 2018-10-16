package com.darja.moviedb.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

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
        const val CATEGORY_POPULAR = "popular"
    }

    constructor(query: String?, category: String?, page: Int, totalPagesCount: Int): this() {
        this.query = query
        this.category = category
        this.page = page
        this.totalPagesCount = totalPagesCount
    }

    override fun toString() = "MovieSearch: query[$query], category[$category], page[$page]"
}
package com.darja.moviedb.api.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.util.*

class ApiGenresList(src: List<ApiGenre>): LinkedList<ApiGenre>(src)

class ApiGenresArrayDeserializer: JsonDeserializer<ApiGenresList> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext): ApiGenresList {
        val genres = json.asJsonObject.getAsJsonArray("genres")
            .map { context.deserialize<ApiGenre>(it, ApiGenre::class.java) }
            .toList()

        return ApiGenresList(genres)
    }
}
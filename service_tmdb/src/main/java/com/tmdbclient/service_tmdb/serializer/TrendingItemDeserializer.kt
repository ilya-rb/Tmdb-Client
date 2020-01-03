package com.tmdbclient.service_tmdb.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.tmdbclient.service_tmdb.model.MovieModel
import com.tmdbclient.service_tmdb.model.PersonModel
import com.tmdbclient.service_tmdb.model.TrendingModel
import java.lang.reflect.Type

class TrendingItemDeserializer : JsonDeserializer<TrendingModel> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): TrendingModel {
        val item = json.asJsonObject

        return when (item.get("media_type")?.asString) {
            "movie" -> context.deserialize(item, MovieModel::class.java)
            "person" -> context.deserialize(item, PersonModel::class.java)
            else -> context.deserialize(item, MovieModel::class.java)
        }
    }
}
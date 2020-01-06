package com.tmdbclient.servicetmdb.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.tmdbclient.servicetmdb.model.MovieModel
import com.tmdbclient.servicetmdb.model.PersonModel
import com.tmdbclient.servicetmdb.model.TrendingModel
import com.tmdbclient.servicetmdb.model.TvShowModel
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
            "tv" -> context.deserialize(item, TvShowModel::class.java)
            else -> context.deserialize(item, TrendingModel.Stub::class.java)
        }
    }
}
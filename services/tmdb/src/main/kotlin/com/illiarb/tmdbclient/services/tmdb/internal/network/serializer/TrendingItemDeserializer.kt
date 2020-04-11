package com.illiarb.tmdbclient.services.tmdb.internal.network.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.MovieModel
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.PersonModel
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.TrendingModel
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.TvShowModel
import java.lang.reflect.Type

internal class TrendingItemDeserializer : JsonDeserializer<TrendingModel> {

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
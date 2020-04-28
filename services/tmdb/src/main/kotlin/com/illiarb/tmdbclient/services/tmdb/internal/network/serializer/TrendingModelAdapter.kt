package com.illiarb.tmdbclient.services.tmdb.internal.network.serializer

import com.illiarb.tmdbclient.services.tmdb.internal.network.model.MovieModelJsonAdapter
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.PersonModelJsonAdapter
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.TrendingModel
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.TvShowModelJsonAdapter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonAdapter.Factory
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

internal class TrendingModelAdapter(moshi: Moshi) : JsonAdapter<List<TrendingModel>>() {

  companion object {
    private val RESULT_NAMES = JsonReader.Options.of("results")
    private val MODEL_NAMES = JsonReader.Options.of("media_type")

    @JvmField
    val FACTORY = Factory { _, _, moshi ->
      TrendingModelAdapter(moshi)
    }
  }

  private val movieAdapter = MovieModelJsonAdapter(moshi)
  private val tvShowAdapter = TvShowModelJsonAdapter(moshi)
  private val personAdapter = PersonModelJsonAdapter(moshi)

  override fun toJson(writer: JsonWriter, value: List<TrendingModel>?) = Unit

  override fun fromJson(reader: JsonReader): List<TrendingModel>? {
    reader.readObject {
      when (reader.selectName(RESULT_NAMES)) {
        0 -> {
          return reader.readArrayToList {
            var element: TrendingModel? = null
            val peaked = reader.peekJson()

            peaked.readObject {
              val mediaType = when (peaked.selectName(MODEL_NAMES)) {
                0 -> peaked.nextString()
                else -> null
              }

              if (mediaType == null) {
                peaked.skipName()
                peaked.skipValue()
              } else {
                element = when (mediaType) {
                  "movie" -> movieAdapter.fromJson(reader)
                  "tv" -> tvShowAdapter.fromJson(reader)
                  "person" -> personAdapter.fromJson(reader)
                  else -> null
                }
              }
            }
            element
          }
        }
        else -> {
          reader.skipName()
          reader.skipValue()
        }
      }
    }
    return emptyList()
  }

  private inline fun JsonReader.readObject(body: () -> Unit) {
    beginObject()
    while (hasNext()) {
      body()
    }
    endObject()
  }

  private inline fun <T : Any> JsonReader.readArrayToList(body: () -> T?): List<T> {
    val result = mutableListOf<T>()
    beginArray()
    while (hasNext()) {
      body()?.let { result.add(it) }
    }
    endArray()
    return result
  }
}
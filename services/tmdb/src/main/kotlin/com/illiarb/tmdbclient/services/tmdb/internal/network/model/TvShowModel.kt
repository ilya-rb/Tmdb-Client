package com.illiarb.tmdbclient.services.tmdb.internal.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class TvShowModel(
  @Json(name = "poster_path") val posterPath: String,
  @Json(name = "name") val name: String
) : TrendingModel
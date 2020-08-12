package com.illiarb.tmdbclient.services.tmdb.internal.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class TvShowDto(
  @Json(name = "poster_path") val posterPath: String,
  @Json(name = "name") val name: String
) : TrendingDto() {

  @Suppress("unused")
  constructor() : this(posterPath = "", name = "")
}
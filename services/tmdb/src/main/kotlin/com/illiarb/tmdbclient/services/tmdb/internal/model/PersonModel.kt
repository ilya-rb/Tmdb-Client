package com.illiarb.tmdbclient.services.tmdb.internal.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class PersonModel(
  @Json(name = "id") val id: Int,
  @Json(name = "name") val name: String,
  @Json(name = "character") val character: String,
  @Json(name = "profile_path") val profilePath: String
) : TrendingModel
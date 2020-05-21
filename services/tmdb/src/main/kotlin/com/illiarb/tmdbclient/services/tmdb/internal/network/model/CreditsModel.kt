package com.illiarb.tmdbclient.services.tmdb.internal.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class CreditsModel(
  @Json(name = "cast") val cast: List<PersonModel>
)
package com.illiarb.tmdbclient.services.tmdb.internal.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ResultsModel<T>(
  @Json(name = "results") val results: List<T>,
  @Json(name = "page") val page: Int,
  @Json(name = "total_pages") val totalPages: Int
)
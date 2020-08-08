package com.illiarb.tmdbclient.services.tmdb.internal.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ResultsDto<T>(
  @Json(name = "results") val results: List<T>,
  @Json(name = "page") val page: Int = 1,
  @Json(name = "total_pages") val totalPages: Int = 1
)
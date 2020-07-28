package com.illiarb.tmdbclient.services.tmdb.internal.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class BackdropDto(
  @Json(name = "file_path") val filePath: String
)

@JsonClass(generateAdapter = true)
internal data class BackdropListDto(
  @Json(name = "backdrops") val backdrops: List<BackdropDto>
)
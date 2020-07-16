package com.illiarb.tmdbclient.services.tmdb.internal.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class BackdropModel(
  @Json(name = "file_path") val filePath: String
)

@JsonClass(generateAdapter = true)
internal data class BackdropListModel(
  @Json(name = "backdrops") val backdrops: List<BackdropModel>
)
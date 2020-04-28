package com.illiarb.tmdbclient.services.tmdb.internal.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ReviewModel(

  @Json(name = "author")
  val author: String,

  @Json(name = "content")
  val content: String,

  @Json(name = "url")
  val url: String

)
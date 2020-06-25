package com.illiarb.tmdbclient.services.tmdb.api.domain

data class Image(
  val baseUrl: String,
  val path: String,
  val sizes: List<String>
) {

  fun buildFullUrl(selectedSize: String): String = buildString {
    append(baseUrl)
    append(selectedSize)
    append(path)
  }
}
package com.illiarb.tmdbclient.services.tmdb.internal.image

internal enum class ImageType(val code: String) {

  Backdrop("backdrop"),

  Poster("poster"),

  Profile("profile");

  override fun toString(): String = code
}

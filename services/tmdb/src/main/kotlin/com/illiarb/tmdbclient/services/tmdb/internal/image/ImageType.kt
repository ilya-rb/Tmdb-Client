package com.illiarb.tmdbclient.services.tmdb.internal.image

internal enum class ImageType {

  Backdrop,

  Poster,

  Profile;

  override fun toString(): String =
    when (this) {
      Backdrop -> "backdrop"
      Poster -> "poster"
      Profile -> "profile"
    }
}

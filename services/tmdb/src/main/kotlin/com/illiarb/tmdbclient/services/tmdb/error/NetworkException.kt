package com.illiarb.tmdbclient.services.tmdb.error

data class NetworkException(val kind: Kind, override val message: String) : Throwable(message) {

  enum class Kind {
    Timeout,
    Io
  }
}
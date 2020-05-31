package com.illiarb.tmdbclient.services.tmdb.error

class TmdbException(override val message: String) : Throwable(message)
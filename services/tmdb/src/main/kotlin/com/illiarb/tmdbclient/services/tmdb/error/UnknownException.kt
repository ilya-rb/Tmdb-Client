package com.illiarb.tmdbclient.services.tmdb.error

data class UnknownException(override val message: String) : Throwable(message)
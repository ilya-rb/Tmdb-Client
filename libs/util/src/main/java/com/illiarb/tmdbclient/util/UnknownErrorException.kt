package com.illiarb.tmdbclient.util

/**
 * @author ilya-rb on 16.01.19.
 */
class UnknownErrorException(override val message: String) : Throwable(message)
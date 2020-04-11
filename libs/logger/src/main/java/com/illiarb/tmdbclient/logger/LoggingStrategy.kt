package com.illiarb.tmdbclient.logger

interface LoggingStrategy {

  fun log(tag: String, priority: LoggerPriority, message: String, throwable: Throwable?)
}
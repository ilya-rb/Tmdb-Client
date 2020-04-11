package com.illiarb.tmdbclient.libs.logger

interface LoggingStrategy {

  fun log(tag: String, priority: LoggerPriority, message: String, throwable: Throwable?)
}
package com.illiarb.tmdbclient.logger

/**
 * @author ilya-rb on 13.11.18.
 */
object Logger {

  private const val TAG_DEFAULT = "[Logger]"

  private val strategies = mutableSetOf<LoggingStrategy>()

  fun addLoggingStrategy(printer: LoggingStrategy) {
    strategies.add(printer)
  }

  fun d(tag: String = TAG_DEFAULT, message: String, t: Throwable? = null) {
    logWithPriority(tag, LoggerPriority.Debug, message, t)
  }

  fun i(tag: String = TAG_DEFAULT, message: String, t: Throwable? = null) {
    logWithPriority(tag, LoggerPriority.Info, message, t)
  }

  fun w(tag: String = TAG_DEFAULT, message: String, t: Throwable? = null) {
    logWithPriority(tag, LoggerPriority.Warn, message, t)
  }

  fun e(tag: String = TAG_DEFAULT, message: String, t: Throwable? = null) {
    logWithPriority(tag, LoggerPriority.Error, message, t)
  }

  private fun logWithPriority(
    tag: String,
    priority: LoggerPriority,
    message: String,
    throwable: Throwable? = null
  ) {
    strategies.forEach {
      it.log(tag, priority, message, throwable)
    }
  }
}
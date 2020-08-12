package com.illiarb.tmdbclient.libs.logger

/**
 * @author ilya-rb on 13.11.18.
 */
object Logger {

  private const val TAG_DEFAULT = "[LOGGER]"

  private val strategies = mutableSetOf<LoggingStrategy>()

  fun addLoggingStrategy(printer: LoggingStrategy) {
    strategies.add(printer)
  }

  fun d(message: String, t: Throwable? = null, tag: String = TAG_DEFAULT) {
    logWithPriority(tag, LoggerPriority.Debug, message, t)
  }

  fun i(message: String, t: Throwable? = null, tag: String = TAG_DEFAULT) {
    logWithPriority(tag, LoggerPriority.Info, message, t)
  }

  fun w(message: String, t: Throwable? = null, tag: String = TAG_DEFAULT) {
    logWithPriority(tag, LoggerPriority.Warn, message, t)
  }

  fun e(message: String, t: Throwable? = null, tag: String = TAG_DEFAULT) {
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
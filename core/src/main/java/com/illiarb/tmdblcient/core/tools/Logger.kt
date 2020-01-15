package com.illiarb.tmdblcient.core.tools

/**
 * @author ilya-rb on 13.11.18.
 */
object Logger {

    private const val TAG_DEFAULT = "Logger"

    private val strategies = mutableSetOf<LoggingStrategy>()

    fun addLoggingStrategy(printer: LoggingStrategy) {
        strategies.add(printer)
    }

    fun d(tag: String = TAG_DEFAULT, message: String, t: Throwable? = null) {
        logWithPriority(tag, Priority.DEBUG, message, t)
    }

    fun i(tag: String = TAG_DEFAULT, message: String, t: Throwable? = null) {
        logWithPriority(tag, Priority.INFO, message, t)
    }

    fun w(tag: String = TAG_DEFAULT, message: String, t: Throwable? = null) {
        logWithPriority(tag, Priority.WARN, message, t)
    }

    fun e(tag: String = TAG_DEFAULT, message: String, t: Throwable? = null) {
        logWithPriority(tag, Priority.ERROR, message, t)
    }

    private fun logWithPriority(tag: String, priority: Priority, message: String, throwable: Throwable? = null) {
        strategies.forEach {
            it.log(tag, priority, message, throwable)
        }
    }

    interface LoggingStrategy {

        fun log(tag: String, priority: Priority, message: String, throwable: Throwable?)
    }

    enum class Priority {
        WARN,
        DEBUG,
        INFO,
        ERROR
    }
}
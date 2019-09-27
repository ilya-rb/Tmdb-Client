package com.illiarb.tmdblcient.core.tools

/**
 * @author ilya-rb on 13.11.18.
 */
object Logger {

    private val strategies = mutableSetOf<LoggingStrategy>()

    fun addLoggingStrategy(printer: LoggingStrategy) {
        strategies.add(printer)
    }

    fun d(message: String, t: Throwable? = null) {
        logWithPriority(Priority.DEBUG, message, t)
    }

    fun i(message: String, t: Throwable? = null) {
        logWithPriority(Priority.INFO, message, t)
    }

    fun w(message: String, t: Throwable? = null) {
        logWithPriority(Priority.WARN, message, t)
    }

    fun e(message: String, t: Throwable? = null) {
        logWithPriority(Priority.ERROR, message, t)
    }

    private fun logWithPriority(priority: Priority, message: String, throwable: Throwable? = null) {
        strategies.forEach {
            it.log(priority, message, throwable)
        }
    }

    interface LoggingStrategy {

        fun log(priority: Priority, message: String, throwable: Throwable?)
    }

    enum class Priority {
        WARN,
        DEBUG,
        INFO,
        ERROR
    }
}
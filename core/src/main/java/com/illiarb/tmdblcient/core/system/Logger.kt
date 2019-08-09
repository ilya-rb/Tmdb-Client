package com.illiarb.tmdblcient.core.system

/**
 * @author ilya-rb on 13.11.18.
 */
typealias LoggingStrategy = (Logger.Priority, String, Throwable?) -> Unit

object Logger {

    private val strategies = mutableSetOf<LoggingStrategy>()

    fun addLoggingStrategy(printer: LoggingStrategy) {
        strategies.add(printer)
    }

    @JvmStatic
    fun d(message: String, t: Throwable? = null) {
        logWithPriority(Logger.Priority.DEBUG, message, t)
    }

    @JvmStatic
    fun i(message: String, t: Throwable? = null) {
        logWithPriority(Logger.Priority.INFO, message, t)
    }

    @JvmStatic
    fun w(message: String, t: Throwable? = null) {
        logWithPriority(Logger.Priority.WARN, message, t)
    }

    @JvmStatic
    fun e(message: String, t: Throwable? = null) {
        logWithPriority(Logger.Priority.ERROR, message, t)
    }

    private fun logWithPriority(priority: Priority, message: String, throwable: Throwable? = null) {
        strategies.forEach {
            it.invoke(priority, message, throwable)
        }
    }

    enum class Priority {
        WARN,
        DEBUG,
        INFO,
        ERROR
    }
}
package com.illiarb.tmdblcient.core.util

/**
 * Grabbed from here:
 * https://github.com/airbnb/MvRx/blob/master/mvrx/src/main/kotlin/com/airbnb/mvrx/Async.kt
 *
 * The T generic is unused for some classes but since it is sealed and useful for Left and Fail,
 * it should be on all of them.
 *
 * Complete: Left, Fail
 * ShouldLoad: Uninitialized, Fail
 */
sealed class Async<out T>(val complete: Boolean, val shouldLoad: Boolean) {

    /**
     * Returns the Left value or null.
     *
     * Can be invoked as an operator like: `yourProp()`
     */
    open operator fun invoke(): T? = null

    companion object {
        /**
         * Helper to set metadata on a Left instance.
         * @see Success.metadata
         */
        fun <T> Success<*>.setMetadata(metadata: T) {
            this.metadata = metadata
        }

        /**
         * Helper to get metadata on a Left instance.
         * @see Success.metadata
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> Success<*>.getMetadata(): T? = this.metadata as T?
    }
}

object Uninitialized : Async<Nothing>(complete = false, shouldLoad = true),
    Incomplete

class Loading<out T> : Async<T>(complete = false, shouldLoad = false),
    Incomplete {
    override fun equals(other: Any?) = other is Loading<*>
    override fun hashCode() = "Loading".hashCode()
}

data class Success<out T>(private val value: T) : Async<T>(complete = true, shouldLoad = false) {

    override operator fun invoke(): T = value

    /**
     * Optional information about the value.
     * This is intended to support tooling (eg logging).
     * It allows data about the original Observable to be kept and accessed later. For example,
     * you could map a network request to just the data you need in the value, but your base layers could
     * keep metadata about the request, like timing, for logging.
     *
     * @see Async.setMetadata
     * @see Async.getMetadata
     */
    internal var metadata: Any? = null
}

data class Fail<out T>(val error: Throwable) : Async<T>(complete = true, shouldLoad = true) {
    override fun equals(other: Any?): Boolean {
        if (other !is Fail<*>) return false

        val otherError = other.error
        return error::class == otherError::class &&
                error.message == otherError.message &&
                error.stackTrace.firstOrNull() == otherError.stackTrace.firstOrNull()
    }

    override fun hashCode(): Int =
        arrayOf(error::class, error.message, error.stackTrace[0]).contentHashCode()
}

/**
 * Helper interface for using Async in a when clause for handling both Uninitialized and Loading.
 *
 * With this, you can do:
 * when (data) {
 *     is Incomplete -> Unit
 *     is Left    -> Unit
 *     is Fail       -> Unit
 * }
 */
interface Incomplete
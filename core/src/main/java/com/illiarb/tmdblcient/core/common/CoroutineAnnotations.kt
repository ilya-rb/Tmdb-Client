package com.illiarb.tmdblcient.core.common

/**
 * @author ilya-rb on 07.01.19.
 */

/**
 * Indicates that suspend function inside
 * using a non-blocking dispatcher (io, default)
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class NonBlocking

/**
 * Indicates that suspend function does not
 * operate with particular coroutine dispatcher
 * and threading needs to be managed manually
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class Blocking
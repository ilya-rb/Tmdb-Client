package com.illiarb.tmdblcient.core.storage

/**
 * @author ilya-rb on 16.01.19.
 */
interface ErrorHandler {

    fun createExceptionFromThrowable(error: Throwable): Throwable
}
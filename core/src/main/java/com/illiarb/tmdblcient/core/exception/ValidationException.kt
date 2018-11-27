package com.illiarb.tmdblcient.core.exception

/**
 * @author ilya-rb on 27.11.18.
 */
data class ValidationException(val errors: List<Pair<Int, String>>) : Throwable()
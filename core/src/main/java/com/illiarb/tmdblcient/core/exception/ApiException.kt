package com.illiarb.tmdblcient.core.exception

/**
 * @author ilya-rb on 19.11.18.
 */
class ApiException(val statusCode: Int, override val message: String) : Throwable(message)
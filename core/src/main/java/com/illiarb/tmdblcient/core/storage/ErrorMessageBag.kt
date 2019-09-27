package com.illiarb.tmdblcient.core.storage

/**
 * @author ilya-rb on 27.11.18.
 */
interface ErrorMessageBag {

    fun getDefaultErrorMessage(): String

    fun getNetworkConnectionMessage(): String
}
package com.illiarb.tmdblcient.core.exception

/**
 * @author ilya-rb on 27.11.18.
 */
interface ErrorMessageBag {

    fun getUsernameEmptyMessage(): String

    fun getPasswordEmptyMessage(): String

    fun getIncorrectPasswordLengthMessage(): String
}
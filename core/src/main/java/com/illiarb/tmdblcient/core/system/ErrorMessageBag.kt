package com.illiarb.tmdblcient.core.system

/**
 * @author ilya-rb on 27.11.18.
 */
interface ErrorMessageBag {

    fun getUsernameEmptyMessage(): String

    fun getPasswordEmptyMessage(): String

    fun getIncorrectPasswordLengthMessage(): String
}
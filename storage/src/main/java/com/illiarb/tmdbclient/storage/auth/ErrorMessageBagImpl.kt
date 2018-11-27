package com.illiarb.tmdbclient.storage.auth

import com.illiarb.tmdbclient.storage.R
import com.illiarb.tmdblcient.core.system.ErrorMessageBag
import com.illiarb.tmdblcient.core.system.ResourceResolver
import javax.inject.Inject

/**
 * @author ilya-rb on 27.11.18.
 */
class ErrorMessageBagImpl @Inject constructor(
    private val resourceResolver: ResourceResolver
) : ErrorMessageBag {

    override fun getUsernameEmptyMessage(): String = resourceResolver.getString(R.string.error_username_empty)

    override fun getPasswordEmptyMessage(): String = resourceResolver.getString(R.string.error_password_empty)

    override fun getIncorrectPasswordLengthMessage(): String =
        resourceResolver.getString(R.string.error_password_incorrect_length)
}
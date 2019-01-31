package com.illiarb.tmdbclient.storage.auth

import com.illiarb.tmdbclient.storage.R
import com.illiarb.tmdblcient.core.storage.ErrorMessageBag
import com.illiarb.tmdblcient.core.storage.ResourceResolver
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

    override fun getDefaultErrorMessage(): String =
        resourceResolver.getString(R.string.error_unknown)

    override fun getNetworkConnectionMessage(): String =
        resourceResolver.getString(R.string.error_bad_connection)
}
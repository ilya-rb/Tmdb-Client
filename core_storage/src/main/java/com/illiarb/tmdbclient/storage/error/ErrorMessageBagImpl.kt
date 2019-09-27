package com.illiarb.tmdbclient.storage.error

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

    override fun getDefaultErrorMessage(): String =
        resourceResolver.getString(R.string.error_unknown)

    override fun getNetworkConnectionMessage(): String =
        resourceResolver.getString(R.string.error_bad_connection)
}
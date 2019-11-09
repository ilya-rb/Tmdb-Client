package com.illiarb.tmdbclient.storage.error

import com.illiarb.tmdbclient.storage.R
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import javax.inject.Inject

/**
 * @author ilya-rb on 27.11.18.
 */
class ErrorMessageBag @Inject constructor(private val resourceResolver: ResourceResolver) {

    fun getDefaultErrorMessage(): String =
        resourceResolver.getString(R.string.error_unknown)

    fun getNetworkConnectionMessage(): String =
        resourceResolver.getString(R.string.error_bad_connection)
}
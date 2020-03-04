package com.illiarb.tmdbclient.storage.error

import com.google.gson.Gson
import com.illiarb.tmdbclient.storage.R
import com.illiarb.tmdblcient.core.exception.ApiException
import com.illiarb.tmdblcient.core.exception.NetworkException
import com.illiarb.tmdblcient.core.exception.UnknownErrorException
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * @author ilya-rb on 16.01.19.
 */
class ErrorHandler @Inject constructor(
  private val gson: Gson,
  private val resourceResolver: ResourceResolver
) {

  fun createFromThrowable(error: Throwable): Throwable =
    when {
      isNetworkError(error) -> NetworkException(resourceResolver.getString(R.string.error_bad_connection))
      error is HttpException -> createNetworkError(error)
      else -> UnknownErrorException(resourceResolver.getString(R.string.error_unknown))
    }

  fun createFromErrorBody(body: ResponseBody?): Throwable =
    if (body == null) {
      UnknownErrorException(resourceResolver.getString(R.string.error_unknown))
    } else {
      gson.fromJson(body.string(), ApiException::class.java)
    }

  private fun createNetworkError(error: HttpException): Throwable {
    val errorBody = error.response()?.errorBody()

    return if (errorBody == null) {
      UnknownErrorException(resourceResolver.getString(R.string.error_unknown))
    } else {
      gson.fromJson(errorBody.string(), ApiException::class.java)
    }
  }

  private fun isNetworkError(throwable: Throwable): Boolean =
    throwable is SocketTimeoutException || throwable is IOException
}
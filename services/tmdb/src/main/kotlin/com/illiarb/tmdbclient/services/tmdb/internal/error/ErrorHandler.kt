package com.illiarb.tmdbclient.services.tmdb.internal.error

import com.illiarb.tmdbclient.libs.util.ApiException
import com.illiarb.tmdbclient.libs.util.NetworkException
import com.illiarb.tmdbclient.libs.util.UnknownErrorException
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * @author ilya-rb on 16.01.19.
 */
class ErrorHandler @Inject constructor(
  private val resourceResolver: com.illiarb.tmdbclient.libs.tools.ResourceResolver,
  private val moshi: Moshi
) {

  fun createFromThrowable(error: Throwable): Throwable =
    when {
      isNetworkError(error) -> NetworkException("")//resourceResolver.getString(R.string.error_bad_connection))
      error is HttpException -> createNetworkError(error)
      else -> UnknownErrorException("")//resourceResolver.getString(R.string.error_unknown))
    }

  fun createFromErrorBody(body: ResponseBody?): Throwable =
    if (body == null) {
      UnknownErrorException("")//resourceResolver.getString(R.string.error_unknown))
    } else {
      ApiException(0, "message")
      //gson.fromJson(body.string(), ApiException::class.java)
    }

  private fun createNetworkError(error: HttpException): Throwable {
    val errorBody = error.response()?.errorBody()

    return if (errorBody == null) {
      UnknownErrorException("")//resourceResolver.getString(R.string.error_unknown))
    } else {
      ApiException(0, "message")
    }
  }

  private fun isNetworkError(throwable: Throwable): Boolean =
    throwable is SocketTimeoutException || throwable is IOException
}
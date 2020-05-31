package com.illiarb.tmdbclient.services.tmdb.internal.error

import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.services.tmdb.R
import com.illiarb.tmdbclient.services.tmdb.error.NetworkException
import com.illiarb.tmdbclient.services.tmdb.error.NetworkException.Kind
import com.illiarb.tmdbclient.services.tmdb.error.TmdbException
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * @author ilya-rb on 16.01.19.
 */
class ErrorCreator @Inject constructor(
  private val resourceResolver: ResourceResolver,
  private val moshi: Moshi
) {

  fun createFromThrowable(throwable: Throwable): Throwable =
    when (throwable) {
      is IOException ->
        NetworkException(Kind.Io, resourceResolver.getString(R.string.error_bad_connection))
      is SocketTimeoutException ->
        NetworkException(Kind.Timeout, resourceResolver.getString(R.string.error_bad_connection))
      is HttpException -> createHttpError(throwable)
      else -> UnknownError(resourceResolver.getString(R.string.error_unknown))
    }

  fun createFromErrorBody(body: ResponseBody?): Throwable =
    if (body == null) {
      UnknownError(resourceResolver.getString(R.string.error_unknown))
    } else {
      val error = moshi.adapter(TmdbError::class.java).fromJson(body.toString())
      if (error == null) {
        UnknownError(resourceResolver.getString(R.string.error_unknown))
      } else {
        TmdbException(error.statusMessage)
      }
    }

  private fun createHttpError(error: HttpException): Throwable {
    val errorBody = error.response()?.errorBody()

    return if (errorBody == null) {
      UnknownError(resourceResolver.getString(R.string.error_unknown))
    } else {
      createFromErrorBody(errorBody)
    }
  }
}
package com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor

import com.illiarb.tmdbclient.services.tmdb.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class ApiKeyInterceptor @Inject constructor() : Interceptor {

  companion object {
    const val QUERY_PARAM_API_KEY = "api_key"
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
      .newBuilder()
      .url(
        chain.request()
          .url
          .newBuilder()
          .addQueryParameter(QUERY_PARAM_API_KEY, BuildConfig.API_KEY)
          .build()
      )
      .build()

    return chain.proceed(request)
  }
}
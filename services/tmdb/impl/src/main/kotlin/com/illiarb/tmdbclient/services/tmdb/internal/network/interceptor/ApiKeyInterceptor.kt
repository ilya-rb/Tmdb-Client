package com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor

import com.illiarb.tmdbclient.libs.buildconfig.TmdbConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class ApiKeyInterceptor @Inject constructor(
  private val tmdbConfig: TmdbConfig
) : Interceptor {

  companion object {
    const val QUERY_PARAM_API_KEY = "api_key"
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
      .newBuilder()
      .url(
        chain.request()
          .url()
          .newBuilder()
          .addQueryParameter(QUERY_PARAM_API_KEY, tmdbConfig.apiKey)
          .build()
      )
      .build()

    return chain.proceed(request)
  }
}
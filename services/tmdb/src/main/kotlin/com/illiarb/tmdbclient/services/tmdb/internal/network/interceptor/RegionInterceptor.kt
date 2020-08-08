package com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor

import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.internal.repository.ConfigurationRepository
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class RegionInterceptor @Inject constructor(
  private val configurationRepository: ConfigurationRepository,
  private val resourceResolver: ResourceResolver
) : Interceptor {

  companion object {
    const val QUERY_PARAM_REGION = "region"
    const val QUERY_PARAM_LANGUAGE = "language"
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val userLanguage = resourceResolver.getUserISOCountry()

    return chain.proceed(
      request.newBuilder()
        .url(
          request.url().newBuilder()
//            .addQueryParameter(QUERY_PARAM_LANGUAGE, userLanguage)
//            .addRegionParameter()
            .build()
        )
        .build()
    )
  }

  private fun HttpUrl.Builder.addRegionParameter(): HttpUrl.Builder {
    val countries = runBlocking { configurationRepository.getCountries() }
    if (countries is Result.Ok) {
      val region = countries.data.find { it.code == resourceResolver.getUserLocale().country }?.code
      if (!region.isNullOrBlank()) {
        addQueryParameter(QUERY_PARAM_REGION, region)
      }
    }
    return this
  }
}
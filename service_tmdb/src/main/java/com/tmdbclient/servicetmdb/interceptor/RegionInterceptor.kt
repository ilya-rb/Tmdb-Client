package com.tmdbclient.servicetmdb.interceptor

import com.illiarb.tmdblcient.core.storage.ResourceResolver
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.repository.ConfigurationRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RegionInterceptor @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val resourceResolver: ResourceResolver
) : Interceptor {

    companion object {
        const val QUERY_PARAM_REGION = "region"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val countries = runBlocking { configurationRepository.getCountries() }
        if (countries is Result.Success) {
            val region = countries.data.find { it.code == resourceResolver.getUserLocaleCode().country }?.code
            if (!region.isNullOrBlank()) {
                return chain.proceed(
                    request.newBuilder()
                        .url(
                            request.url.newBuilder()
                                .addQueryParameter(QUERY_PARAM_REGION, region)
                                .build()
                        )
                        .build()
                )
            }
        }

        return chain.proceed(request)
    }
}
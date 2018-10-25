package com.illiarb.tmdbclient.storage.network.api

import com.illiarb.tmdbclient.storage.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .url(
                chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", BuildConfig.API_KEY)
                    .build()
            )
            .build()

        return chain.proceed(request)
    }
}
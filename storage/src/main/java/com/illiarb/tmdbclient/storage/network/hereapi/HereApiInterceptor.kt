package com.illiarb.tmdbclient.storage.network.hereapi

import com.illiarb.tmdbclient.storage.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * @author ilya-rb on 02.11.18.
 */
class HereApiInterceptor @Inject constructor() : Interceptor {

    companion object {
        const val PARAM_APP_ID = "app_id"
        const val PARAM_APP_CODE = "app_code"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .url(
                chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter(PARAM_APP_ID, BuildConfig.HERE_APP_ID)
                    .addQueryParameter(PARAM_APP_CODE, BuildConfig.HERE_APP_CODE)
                    .build()
            )
            .build()

        return chain.proceed(request)
    }
}
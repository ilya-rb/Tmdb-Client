package com.illiarb.tmdbclient.storage.di.modules

import com.google.gson.Gson
import com.illiarb.tmdbclient.storage.error.ErrorHandler
import com.illiarb.tmdbclient.storage.network.ApiCallAdapterFactory
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter

/**
 * @author ilya-rb on 24.12.18.
 */
@Module
class NetworkModule {

    companion object {

        const val CONNECT_TIMEOUT = 10L
        const val READ_TIMEOUT = 10L
        const val WRITE_TIMEOUT = 10L

        @Provides
        @JvmStatic
        fun provideApiCallAdapterFactory(errorHandler: ErrorHandler): CallAdapter.Factory =
            ApiCallAdapterFactory(errorHandler)

        @Provides
        @JvmStatic
        fun provideLoggerInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

        @Provides
        @JvmStatic
        fun provideErrorHandler(gson: Gson, resolver: ResourceResolver): ErrorHandler =
            ErrorHandler(gson, resolver)
    }
}
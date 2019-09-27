package com.illiarb.tmdbclient.storage.di.modules

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter

/**
 * @author ilya-rb on 24.12.18.
 */
@Module
class NetworkModule {

    @Module
    companion object {

        const val CONNECT_TIMEOUT = 10L
        const val READ_TIMEOUT = 10L
        const val WRITE_TIMEOUT = 10L

        @Provides
        @JvmStatic
        fun provideApiCallAdapterFactory(): CallAdapter.Factory = CoroutineCallAdapterFactory()

        @Provides
        @JvmStatic
        fun provideLoggerInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
    }
}
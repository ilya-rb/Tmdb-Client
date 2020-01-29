package com.tmdbclient.servicetmdb.di

import com.illiarb.tmdbclient.storage.di.modules.NetworkModule
import com.tmdbclient.servicetmdb.BuildConfig
import com.tmdbclient.servicetmdb.api.ConfigurationApi
import com.tmdbclient.servicetmdb.interceptor.ApiKeyInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Module
object ConfigurationModule {

    @Provides
    @JvmStatic
    fun provideConfigurationApi(@ConfigurationClient retrofit: Retrofit): ConfigurationApi =
        retrofit.create(ConfigurationApi::class.java)

    @Provides
    @ConfigurationClient
    fun provideConfigurationApiRetrofit(
        @ConfigurationClient
        okHttpClient: OkHttpClient,
        callAdapterFactory: CallAdapter.Factory,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @ConfigurationClient
    @JvmStatic
    fun provideConfigurationApiOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(NetworkModule.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NetworkModule.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NetworkModule.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    private annotation class ConfigurationClient
}
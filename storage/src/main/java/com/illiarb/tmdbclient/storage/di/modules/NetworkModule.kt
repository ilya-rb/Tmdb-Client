package com.illiarb.tmdbclient.storage.di.modules

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.illiarb.tmdbclient.storage.BuildConfig
import com.illiarb.tmdbclient.storage.network.api.ApiKeyInterceptor
import com.illiarb.tmdbclient.storage.network.api.service.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

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
        @Singleton
        fun provideMoviesService(retrofit: Retrofit): MovieService =
            retrofit.create(MovieService::class.java)

        @Provides
        @JvmStatic
        @Singleton
        fun provideSearchService(retrofit: Retrofit): SearchService =
            retrofit.create(SearchService::class.java)

        @Provides
        @JvmStatic
        @Singleton
        fun provideAuthService(retrofit: Retrofit): AuthService =
            retrofit.create(AuthService::class.java)

        @Provides
        @JvmStatic
        @Singleton
        fun provideAccountService(retrofit: Retrofit): AccountService =
            retrofit.create(AccountService::class.java)

        @Provides
        @JvmStatic
        @Singleton
        fun provideConfigurationService(retrofit: Retrofit): ConfigurationService =
            retrofit.create(ConfigurationService::class.java)

        @Provides
        @JvmStatic
        @Singleton
        fun provideApiRetrofit(
            okHttpClient: OkHttpClient,
            callAdapterFactory: CallAdapter.Factory,
            converterFactory: Converter.Factory
        ): Retrofit =
            Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(converterFactory)
                .build()

        @Provides
        @JvmStatic
        fun provideApiOkHttpClient(
            apiKeyInterceptor: ApiKeyInterceptor,
            httpLoggerInterceptor: HttpLoggingInterceptor
        ): OkHttpClient =
            OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(apiKeyInterceptor)
                .addInterceptor(httpLoggerInterceptor)
                .build()

        @Provides
        @JvmStatic
        fun provideApiCallAdapterFactory(): CallAdapter.Factory = CoroutineCallAdapterFactory.invoke()

        @Provides
        @JvmStatic
        fun provideApiConverterFactory(gson: Gson): Converter.Factory = GsonConverterFactory.create(gson)

        @Provides
        @JvmStatic
        fun provideLoggerInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        @Provides
        @JvmStatic
        fun provideApiGson(): Gson =
            GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
    }
}
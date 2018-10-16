package com.illiarb.tmdbclient.network.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.illiarb.tmdbclient.network.BuildConfig
import com.illiarb.tmdbclient.network.api.ApiKeyInterceptor
import com.illiarb.tmdbclient.network.api.movie.MovieService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Module
    companion object {

        const val CONNECT_TIMEOUT = 10L
        const val READ_TIMEOUT = 10L
        const val WRITE_TIMEOUT = 10L

        @Provides
        @JvmStatic
        fun provideMoviesService(retrofit: Retrofit): MovieService =
            retrofit.create(MovieService::class.java)

        @Provides
        @JvmStatic
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
        fun provideApiCallAdapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

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
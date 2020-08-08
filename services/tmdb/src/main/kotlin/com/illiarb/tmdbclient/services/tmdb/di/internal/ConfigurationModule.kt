package com.illiarb.tmdbclient.services.tmdb.di.internal

import com.illiarb.tmdbclient.libs.buildconfig.TmdbConfig
import com.illiarb.tmdbclient.services.tmdb.di.NetworkInterceptor
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.ConfigurationApi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Module
internal object ConfigurationModule {

  private const val TIMEOUT_SECONDS = 10L

  @Provides
  @JvmStatic
  fun provideConfigurationApi(@ConfigurationClient retrofit: Retrofit): ConfigurationApi =
    retrofit.create(ConfigurationApi::class.java)

  @Provides
  @ConfigurationClient
  @JvmStatic
  fun provideConfigurationApiRetrofit(
    @ConfigurationClient
    okHttpClient: Lazy<OkHttpClient>,
    callAdapterFactory: CallAdapter.Factory,
    converterFactory: Converter.Factory,
    tmdbConfig: TmdbConfig
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl(tmdbConfig.apiUrl)
      .addCallAdapterFactory(callAdapterFactory)
      .callFactory { okHttpClient.get().newCall(it) }
      .addConverterFactory(converterFactory)
      .build()
  }

  @Provides
  @ConfigurationClient
  @JvmStatic
  fun provideConfigurationApiOkHttpClient(
    interceptors: Set<@JvmSuppressWildcards Interceptor>,
    @NetworkInterceptor networkInterceptors: Set<@JvmSuppressWildcards Interceptor>
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .apply {
        interceptors.forEach {
          addInterceptor(it)
        }

        networkInterceptors.forEach {
          addNetworkInterceptor(it)
        }
      }
      .build()
  }

  @Qualifier
  @Retention(AnnotationRetention.RUNTIME)
  private annotation class ConfigurationClient
}
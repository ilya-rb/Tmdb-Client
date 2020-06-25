package com.illiarb.tmdbclient.services.tmdb.di

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.illiarb.tmdbclient.libs.buildconfig.TmdbConfig
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.ConfigurationApi
import com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor.ApiKeyInterceptor
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Module
object ConfigurationModule {

  private const val TIMEOUT_SECONDS = 10L

  @Provides
  @JvmStatic
  internal fun provideConfigurationApi(@ConfigurationClient retrofit: Retrofit): ConfigurationApi =
    retrofit.create(ConfigurationApi::class.java)

  @Provides
  @ConfigurationClient
  @JvmStatic
  internal fun provideConfigurationApiRetrofit(
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
  internal fun provideConfigurationApiOkHttpClient(
    apiKeyInterceptor: ApiKeyInterceptor,
    flipperOkHttpInterceptor: FlipperOkhttpInterceptor
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .addInterceptor(apiKeyInterceptor)
      .addNetworkInterceptor(flipperOkHttpInterceptor)
      .build()
  }

  @Qualifier
  @Retention(AnnotationRetention.RUNTIME)
  private annotation class ConfigurationClient
}
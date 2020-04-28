package com.illiarb.tmdbclient.services.tmdb.di

import android.app.Application
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.error.ErrorHandler
import com.illiarb.tmdbclient.services.tmdb.internal.network.CallAdapterFactory
import com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor.ApiKeyInterceptor
import com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor.RegionInterceptor
import com.illiarb.tmdbclient.services.tmdb.internal.network.serializer.TrendingModelAdapter
import com.illiarb.tmdbclient.services.tmdb.internal.repository.ConfigurationRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object NetworkModule {

  private const val CACHE_SIZE_BYTES = 20 * 1024L
  private const val TIMEOUT_SECONDS = 10L

  @Provides
  @JvmStatic
  internal fun provideApiKeyInterceptor(): ApiKeyInterceptor = ApiKeyInterceptor()

  @Provides
  @JvmStatic
  internal fun provideRegionInterceptor(
    configurationRepository: ConfigurationRepository,
    resourceResolver: ResourceResolver
  ): RegionInterceptor = RegionInterceptor(configurationRepository, resourceResolver)

  @Provides
  @JvmStatic
  @Singleton
  internal fun provideMoshi(): Moshi {
    return Moshi.Builder()
      .add(TrendingModelAdapter.FACTORY)
      .build()
  }

  @Provides
  @JvmStatic
  internal fun provideCallAdapterFactory(errorHandler: ErrorHandler): CallAdapter.Factory {
    return CallAdapterFactory(errorHandler)
  }

  @Provides
  @JvmStatic
  internal fun provideApiConverterFactory(moshi: Moshi): Converter.Factory =
    MoshiConverterFactory.create(moshi)

  @Provides
  @Singleton
  @JvmStatic
  internal fun provideTmdbCache(app: Application): TmdbCache = TmdbCache(app)

  @Provides
  @JvmStatic
  internal fun provideTmdbOkHttpClient(
    app: Application,
    apiKeyInterceptor: ApiKeyInterceptor,
    regionInterceptor: RegionInterceptor,
    flipperOkHttpInterceptor: FlipperOkhttpInterceptor
  ): OkHttpClient =
    OkHttpClient.Builder()
      .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .addInterceptor(apiKeyInterceptor)
      .addInterceptor(regionInterceptor)
      .addNetworkInterceptor(flipperOkHttpInterceptor)
      .retryOnConnectionFailure(true)
      .cache(Cache(app.filesDir, CACHE_SIZE_BYTES))
      .build()
}
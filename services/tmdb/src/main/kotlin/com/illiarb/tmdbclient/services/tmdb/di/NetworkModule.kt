package com.illiarb.tmdbclient.services.tmdb.di

import android.app.Application
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.error.ErrorHandler
import com.illiarb.tmdbclient.services.tmdb.internal.network.CallAdapterFactory
import com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor.ApiKeyInterceptor
import com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor.RegionInterceptor
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.TrendingModel
import com.illiarb.tmdbclient.services.tmdb.internal.network.serializer.TrendingItemDeserializer
import com.illiarb.tmdbclient.services.tmdb.internal.repository.ConfigurationRepository
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
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
  internal fun provideTmdbGson(trendingDeserializer: TrendingItemDeserializer): Gson =
    GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .registerTypeAdapter(TrendingModel::class.java, trendingDeserializer)
      .create()

  @Provides
  @JvmStatic
  internal fun provideCallAdapterFactory(errorHandler: ErrorHandler): CallAdapter.Factory {
    return CallAdapterFactory(errorHandler)
  }

  @Provides
  @JvmStatic
  internal fun provideApiConverterFactory(gson: Gson): Converter.Factory =
    GsonConverterFactory.create(gson)

  @Provides
  @JvmStatic
  internal fun provideTrendingItemDeserializer(): TrendingItemDeserializer =
    TrendingItemDeserializer()

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
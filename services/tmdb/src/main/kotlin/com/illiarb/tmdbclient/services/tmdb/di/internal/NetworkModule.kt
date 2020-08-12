package com.illiarb.tmdbclient.services.tmdb.di.internal

import android.app.Application
import com.illiarb.tmdbclient.libs.buildconfig.TmdbConfig
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.services.tmdb.di.NetworkInterceptor
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.dto.MovieDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.TrendingDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.TvShowDto
import com.illiarb.tmdbclient.services.tmdb.internal.error.ErrorCreator
import com.illiarb.tmdbclient.services.tmdb.internal.network.CallAdapterFactory
import com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor.ApiKeyInterceptor
import com.illiarb.tmdbclient.services.tmdb.internal.network.interceptor.RegionInterceptor
import com.illiarb.tmdbclient.services.tmdb.internal.repository.ConfigurationRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import dagger.multibindings.Multibinds
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
internal interface NetworkModule {

  @Multibinds
  @NetworkInterceptor
  fun bindNetworkInterceptors(): Set<Interceptor>

  @Module
  companion object {

    private const val CACHE_SIZE_BYTES = 20 * 1024L
    private const val TIMEOUT_SECONDS = 10L

    @Provides
    @IntoSet
    @JvmStatic
    fun provideApiKeyInterceptor(tmdbConfig: TmdbConfig): Interceptor =
      ApiKeyInterceptor(tmdbConfig)

    @Provides
    @IntoSet
    @JvmStatic
    fun provideRegionInterceptor(
      configurationRepository: ConfigurationRepository,
      resourceResolver: ResourceResolver
    ): Interceptor = RegionInterceptor(configurationRepository, resourceResolver)

    @Provides
    @JvmStatic
    fun provideMoshi(): Moshi {
      return Moshi.Builder()
        .add(
          PolymorphicJsonAdapterFactory.of(TrendingDto::class.java, "media_type")
            .withSubtype(MovieDto::class.java, "movie")
            .withSubtype(TvShowDto::class.java, "tv")
        )
        .add(KotlinJsonAdapterFactory())
        .build()
    }

    @Provides
    @JvmStatic
    fun provideCallAdapterFactory(errorCreator: ErrorCreator): CallAdapter.Factory {
      return CallAdapterFactory(errorCreator)
    }

    @Provides
    @JvmStatic
    fun provideApiConverterFactory(moshi: Moshi): Converter.Factory =
      MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    @JvmStatic
    fun provideTmdbCache(app: Application): TmdbCache = TmdbCache(app)

    @Provides
    @JvmStatic
    fun provideTmdbOkHttpClient(
      app: Application,
      interceptors: Set<@JvmSuppressWildcards Interceptor>,
      @NetworkInterceptor networkInterceptors: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient =
      OkHttpClient.Builder()
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
        .retryOnConnectionFailure(true)
        .cache(Cache(app.filesDir, CACHE_SIZE_BYTES))
        .build()
  }
}
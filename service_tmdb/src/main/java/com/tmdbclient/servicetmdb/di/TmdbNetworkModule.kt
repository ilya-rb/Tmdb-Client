package com.tmdbclient.servicetmdb.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.illiarb.tmdbclient.storage.di.modules.NetworkModule
import com.illiarb.tmdblcient.core.app.App
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.interceptor.ApiKeyInterceptor
import com.tmdbclient.servicetmdb.interceptor.RegionInterceptor
import com.tmdbclient.servicetmdb.model.TrendingModel
import com.tmdbclient.servicetmdb.repository.ConfigurationRepository
import com.tmdbclient.servicetmdb.serializer.TrendingItemDeserializer
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class TmdbNetworkModule(private val app: App) {

  companion object {

    const val CACHE_SIZE_BYTES = 20 * 1024L

    @Provides
    @JvmStatic
    fun provideApiKeyInterceptor(): ApiKeyInterceptor = ApiKeyInterceptor()

    @Provides
    @JvmStatic
    fun provideRegionInterceptor(
      configurationRepository: ConfigurationRepository,
      resourceResolver: ResourceResolver
    ): RegionInterceptor = RegionInterceptor(configurationRepository, resourceResolver)

    @Provides
    @JvmStatic
    fun provideTmdbGson(trendingDeserializer: TrendingItemDeserializer): Gson =
      GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapter(TrendingModel::class.java, trendingDeserializer)
        .create()

    @Provides
    @JvmStatic
    fun provideApiConverterFactory(gson: Gson): Converter.Factory = GsonConverterFactory.create(gson)

    @Provides
    @JvmStatic
    fun provideTrendingItemDeserializer(): TrendingItemDeserializer = TrendingItemDeserializer()
  }

  @Provides
  @Singleton
  fun provideTmdbCache(): TmdbCache = TmdbCache(app.getApplication())

  @Provides
  fun provideTmdbOkHttpClient(
    apiKeyInterceptor: ApiKeyInterceptor,
    regionInterceptor: RegionInterceptor,
    httpLoggingInterceptor: HttpLoggingInterceptor
  ): OkHttpClient =
    OkHttpClient.Builder()
      .connectTimeout(NetworkModule.CONNECT_TIMEOUT, TimeUnit.SECONDS)
      .readTimeout(NetworkModule.READ_TIMEOUT, TimeUnit.SECONDS)
      .writeTimeout(NetworkModule.WRITE_TIMEOUT, TimeUnit.SECONDS)
      .addInterceptor(apiKeyInterceptor)
      .addInterceptor(httpLoggingInterceptor)
      .addInterceptor(regionInterceptor)
      .retryOnConnectionFailure(true)
      .cache(Cache(app.getApplication().filesDir, CACHE_SIZE_BYTES))
      .build()
}
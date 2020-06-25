package com.illiarb.tmdbclient.services.tmdb.di

import com.illiarb.tmdbclient.libs.buildconfig.TmdbConfig
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.DiscoverApi
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.GenreApi
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.MovieApi
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.TrendingApi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit

@Module
object ApiModule {

  @Provides
  @JvmStatic
  internal fun provideMoviesApi(retrofit: Retrofit): MovieApi =
    retrofit.create(MovieApi::class.java)

  @Provides
  @JvmStatic
  internal fun provideTrendingApi(retrofit: Retrofit): TrendingApi =
    retrofit.create(TrendingApi::class.java)

  @Provides
  @JvmStatic
  internal fun provideDiscoverApi(retrofit: Retrofit): DiscoverApi =
    retrofit.create(DiscoverApi::class.java)

  @Provides
  @JvmStatic
  internal fun provideGenresApi(retrofit: Retrofit): GenreApi =
    retrofit.create(GenreApi::class.java)

  @Provides
  @JvmStatic
  internal fun provideTmdbRetrofit(
    okHttpClient: Lazy<OkHttpClient>,
    callAdapterFactory: CallAdapter.Factory,
    converterFactory: Converter.Factory,
    tmdbConfig: TmdbConfig
  ): Retrofit =
    Retrofit.Builder()
      .baseUrl(tmdbConfig.apiUrl)
      .addCallAdapterFactory(callAdapterFactory)
      .callFactory { okHttpClient.get().newCall(it) }
      .addConverterFactory(converterFactory)
      .build()
}
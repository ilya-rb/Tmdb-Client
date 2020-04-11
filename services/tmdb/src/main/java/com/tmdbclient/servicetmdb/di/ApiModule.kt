package com.tmdbclient.servicetmdb.di

import com.illiarb.tmdbclient.tools.DateFormatter
import com.tmdbclient.servicetmdb.BuildConfig
import com.tmdbclient.servicetmdb.internal.network.api.DiscoverApi
import com.tmdbclient.servicetmdb.internal.network.api.GenreApi
import com.tmdbclient.servicetmdb.internal.network.api.MovieApi
import com.tmdbclient.servicetmdb.internal.network.api.TrendingApi
import com.tmdbclient.servicetmdb.internal.util.TmdbDateFormatter
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
    okHttpClient: OkHttpClient,
    callAdapterFactory: CallAdapter.Factory,
    converterFactory: Converter.Factory
  ): Retrofit =
    Retrofit.Builder()
      .baseUrl(BuildConfig.API_URL)
      .addCallAdapterFactory(callAdapterFactory)
      .client(okHttpClient)
      .addConverterFactory(converterFactory)
      .build()

  @Provides
  @JvmStatic
  internal fun provideDateFormatter(): DateFormatter = TmdbDateFormatter()
}
package com.tmdbclient.servicetmdb.di

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.illiarb.tmdblcient.core.tools.WorkerCreator
import com.illiarb.tmdblcient.core.util.DateFormatter
import com.tmdbclient.servicetmdb.BuildConfig
import com.tmdbclient.servicetmdb.api.DiscoverApi
import com.tmdbclient.servicetmdb.api.GenreApi
import com.tmdbclient.servicetmdb.api.MovieApi
import com.tmdbclient.servicetmdb.api.TrendingApi
import com.tmdbclient.servicetmdb.configuration.ConfigurationFetchWork
import com.tmdbclient.servicetmdb.repository.ConfigurationRepository
import com.tmdbclient.servicetmdb.util.TmdbDateFormatter
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
  fun provideMoviesApi(retrofit: Retrofit): MovieApi =
    retrofit.create(MovieApi::class.java)

  @Provides
  @JvmStatic
  fun provideTrendingApi(retrofit: Retrofit): TrendingApi =
    retrofit.create(TrendingApi::class.java)

  @Provides
  @JvmStatic
  fun provideDiscoverApi(retrofit: Retrofit): DiscoverApi =
    retrofit.create(DiscoverApi::class.java)

  @Provides
  @JvmStatic
  fun provideGenresApi(retrofit: Retrofit): GenreApi =
    retrofit.create(GenreApi::class.java)

  @Provides
  @JvmStatic
  fun provideTmdbRetrofit(
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
  fun provideConfigurationWorkerCreator(configurationRepository: ConfigurationRepository): WorkerCreator {
    return object : WorkerCreator {
      override fun createWorkRequest(context: Context, params: WorkerParameters): Worker {
        return ConfigurationFetchWork(context, params, configurationRepository)
      }
    }
  }

  @Provides
  @JvmStatic
  fun provideDateFormatter(): DateFormatter = TmdbDateFormatter()
}
package com.tmdbclient.servicetmdb.di

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.illiarb.tmdbclient.tools.WorkManager
import com.tmdbclient.servicetmdb.BuildConfig
import com.tmdbclient.servicetmdb.internal.configuration.ConfigurationFetchWork
import com.tmdbclient.servicetmdb.internal.network.api.ConfigurationApi
import com.tmdbclient.servicetmdb.internal.network.interceptor.ApiKeyInterceptor
import com.tmdbclient.servicetmdb.internal.repository.ConfigurationRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    okHttpClient: OkHttpClient,
    callAdapterFactory: CallAdapter.Factory,
    converterFactory: Converter.Factory
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl(BuildConfig.API_URL)
      .addCallAdapterFactory(callAdapterFactory)
      .client(okHttpClient)
      .addConverterFactory(converterFactory)
      .build()
  }

  @Provides
  @ConfigurationClient
  @JvmStatic
  internal fun provideConfigurationApiOkHttpClient(
    apiKeyInterceptor: ApiKeyInterceptor,
    httpLoggingInterceptor: HttpLoggingInterceptor
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
      .addInterceptor(apiKeyInterceptor)
      .addInterceptor(httpLoggingInterceptor)
      .build()
  }

  @Provides
  @JvmStatic
  internal fun provideConfigurationWorkerCreator(
    configurationRepository: ConfigurationRepository
  ): WorkManager.WorkerCreator {
    return object : WorkManager.WorkerCreator {
      override fun createWorkRequest(context: Context, params: WorkerParameters): Worker {
        return ConfigurationFetchWork(context, params, configurationRepository)
      }
    }
  }

  @Qualifier
  @Retention(AnnotationRetention.RUNTIME)
  private annotation class ConfigurationClient
}
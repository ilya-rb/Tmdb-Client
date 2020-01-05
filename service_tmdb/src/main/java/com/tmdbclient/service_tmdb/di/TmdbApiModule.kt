package com.tmdbclient.service_tmdb.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.illiarb.tmdbclient.storage.di.modules.NetworkModule
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.storage.WorkManager
import com.illiarb.tmdblcient.core.storage.WorkRequestCreator
import com.illiarb.tmdblcient.core.storage.WorkerCreator
import com.tmdbclient.service_tmdb.BuildConfig
import com.tmdbclient.service_tmdb.api.ApiKeyInterceptor
import com.tmdbclient.service_tmdb.api.ConfigurationApi
import com.tmdbclient.service_tmdb.api.DiscoverApi
import com.tmdbclient.service_tmdb.api.GenreApi
import com.tmdbclient.service_tmdb.api.MovieApi
import com.tmdbclient.service_tmdb.api.TrendingApi
import com.tmdbclient.service_tmdb.cache.TmdbCache
import com.tmdbclient.service_tmdb.configuration.ConfigurationFetchWork
import com.tmdbclient.service_tmdb.model.TrendingModel
import com.tmdbclient.service_tmdb.serializer.TrendingItemDeserializer
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class TmdbApiModule(val app: App) {

    @Provides
    @Singleton
    fun provideTmdbCache(): TmdbCache = TmdbCache(app)

    @Provides
    fun provideTmdbOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(NetworkModule.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NetworkModule.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NetworkModule.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .cache(Cache(app.getApplication().filesDir, CACHE_SIZE_BYTES))
            .build()

    @Module
    companion object {

        const val CACHE_SIZE_BYTES = 20 * 1024L

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
        fun provideConfigurationApi(retrofit: Retrofit): ConfigurationApi =
            retrofit.create(ConfigurationApi::class.java)

        @Provides
        @JvmStatic
        fun provideTrendingItemDeserializer(): TrendingItemDeserializer =
            TrendingItemDeserializer()

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
        fun provideApiKeyInterceptor(): ApiKeyInterceptor = ApiKeyInterceptor()

        @Provides
        @JvmStatic
        fun provideTmdbGson(trendingDeserializer: TrendingItemDeserializer): Gson =
            GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(TrendingModel::class.java, trendingDeserializer)
                .create()

        @Provides
        @JvmStatic
        fun provideApiConverterFactory(gson: Gson): Converter.Factory =
            GsonConverterFactory.create(gson)

        @Provides
        @JvmStatic
        fun provideConfigurationFetchWorker(
            configurationApi: ConfigurationApi,
            cache: TmdbCache
        ): WorkManager.Worker {
            return object : WorkManager.Worker {

                override fun isWorkerSuitable(workerClassName: String): Boolean =
                    workerClassName == ConfigurationFetchWork::class.java.name

                override val workRequestCreator: WorkRequestCreator
                    get() = { ConfigurationFetchWork.createWorkRequest() }

                override val workCreator: WorkerCreator
                    get() = { context, workerParameters ->
                        ConfigurationFetchWork(
                            context,
                            workerParameters,
                            configurationApi,
                            cache
                        )
                    }
            }
        }
    }
}
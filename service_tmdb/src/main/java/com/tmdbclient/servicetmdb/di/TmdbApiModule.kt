package com.tmdbclient.servicetmdb.di

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.illiarb.tmdbclient.storage.di.modules.NetworkModule
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import com.illiarb.tmdblcient.core.tools.WorkerCreator
import com.tmdbclient.servicetmdb.BuildConfig
import com.tmdbclient.servicetmdb.api.DiscoverApi
import com.tmdbclient.servicetmdb.api.GenreApi
import com.tmdbclient.servicetmdb.api.MovieApi
import com.tmdbclient.servicetmdb.api.TrendingApi
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.configuration.ConfigurationFetchWork
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
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@Suppress("TooManyFunctions")
class TmdbApiModule(val app: App) {

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
        fun provideTrendingItemDeserializer(): TrendingItemDeserializer = TrendingItemDeserializer()

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
        fun provideConfigurationWorkerCreator(configurationRepository: ConfigurationRepository): WorkerCreator {
            return object : WorkerCreator {
                override fun createWorkRequest(context: Context, params: WorkerParameters): Worker {
                    return ConfigurationFetchWork(context, params, configurationRepository)
                }
            }
        }
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
            .cache(Cache(app.getApplication().filesDir, CACHE_SIZE_BYTES))
            .build()
}
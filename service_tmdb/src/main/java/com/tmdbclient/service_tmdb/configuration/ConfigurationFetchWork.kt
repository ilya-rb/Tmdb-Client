package com.tmdbclient.service_tmdb.configuration

import android.content.Context
import androidx.work.*
import com.tmdbclient.service_tmdb.api.ConfigurationApi
import com.tmdbclient.service_tmdb.cache.TmdbCache
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author ilya-rb on 03.12.18.
 */
class ConfigurationFetchWork(
    context: Context,
    workerParameters: WorkerParameters,
    private val configurationService: ConfigurationApi,
    private val persistableStorage: TmdbCache
) : Worker(context, workerParameters) {

    companion object {

        // 2 Days
        private const val WORKER_REPEAT_INTERVAL = 2L

        fun createWorkRequest(): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<ConfigurationFetchWork>(
                WORKER_REPEAT_INTERVAL, TimeUnit.DAYS
            )
                .addTag(ConfigurationFetchWork::class.java.name)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
    }

    override fun doWork(): Result {
        return try {
            runBlocking {
                val configuration = configurationService.getConfiguration().await()
                persistableStorage.storeConfiguration(configuration)
            }
            Result.success()
        } catch (e: IOException) {
            Result.retry()
        }
    }
}
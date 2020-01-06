package com.tmdbclient.servicetmdb.configuration

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.illiarb.tmdblcient.core.storage.WorkManager
import com.tmdbclient.servicetmdb.api.ConfigurationApi
import com.tmdbclient.servicetmdb.cache.TmdbCache
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
        private const val REPEAT_INTERVAL = 2L

        fun createWorkRequest(): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<ConfigurationFetchWork>(REPEAT_INTERVAL, TimeUnit.DAYS)
                .addTag(WorkManager.WorkType.ConfigurationFetch.code)
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
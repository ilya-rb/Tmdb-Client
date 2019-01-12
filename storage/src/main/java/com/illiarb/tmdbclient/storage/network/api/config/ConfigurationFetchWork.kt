package com.illiarb.tmdbclient.storage.network.api.config

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbclient.storage.network.api.service.ConfigurationService
import com.illiarb.tmdblcient.core.system.Logger
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author ilya-rb on 03.12.18.
 */
class ConfigurationFetchWork constructor(
    context: Context,
    workerParameters: WorkerParameters,
    private val configurationService: ConfigurationService,
    private val persistableStorage: PersistableStorage
) : Worker(context, workerParameters) {

    companion object {

        // 2 Days
        const val WORKER_REPEAT_INTERVAL = 2L

        fun createWorkRequest(): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<ConfigurationFetchWork>(WORKER_REPEAT_INTERVAL, TimeUnit.DAYS)
                .addTag(ConfigurationFetchWork::class.java.name)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
    }

    override fun doWork(): Result {
        Logger.i("Starting configuration fetch work..")

        return try {
            runBlocking {
                val configuration = configurationService.getConfiguration().await()
                persistableStorage.storeConfiguration(configuration)
            }

            Logger.i("Successful configuration fetch")

            Result.SUCCESS
        } catch (e: IOException) {
            Logger.e("Error during configuration fetching, rescheduling..", e)

            Result.RETRY
        }
    }
}
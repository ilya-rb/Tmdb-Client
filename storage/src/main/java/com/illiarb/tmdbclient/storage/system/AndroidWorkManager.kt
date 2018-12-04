package com.illiarb.tmdbclient.storage.system

import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkerFactory
import com.illiarb.tmdbclient.storage.network.api.config.ConfigurationFetchWork
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdblcient.core.system.WorkManager
import javax.inject.Inject
import androidx.work.WorkManager as AdWorkManager

/**
 * @author ilya-rb on 03.12.18.
 */
class AndroidWorkManager @Inject constructor(
    private val app: App,
    private val workerFactory: WorkerFactory
) : WorkManager {

    override fun initialize() {
        val configuration = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

        AdWorkManager.initialize(app.getApplication(), configuration)
    }

    override fun schedulerPeriodicConfigurationFetch() {
        AdWorkManager.getInstance()
            .enqueueUniquePeriodicWork(
                ConfigurationFetchWork::class.java.name,
                ExistingPeriodicWorkPolicy.KEEP,
                ConfigurationFetchWork.createWorkRequest()
            )
    }
}
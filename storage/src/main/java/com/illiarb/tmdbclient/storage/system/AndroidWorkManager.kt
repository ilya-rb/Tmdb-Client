package com.illiarb.tmdbclient.storage.system

import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkerFactory
import com.illiarb.tmdbclient.storage.network.api.config.ConfigurationFetchWork
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.system.WorkManager
import javax.inject.Inject
import javax.inject.Singleton
import androidx.work.WorkManager as AdWorkManager

/**
 * @author ilya-rb on 03.12.18.
 */
@Singleton
class AndroidWorkManager @Inject constructor(
    app: App,
    workerFactory: WorkerFactory
) : WorkManager {

    init {
        AdWorkManager.initialize(
            app.getApplication(),
            Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
        )
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
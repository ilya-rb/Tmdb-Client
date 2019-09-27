package com.illiarb.tmdbclient.work

import android.util.Log
import androidx.work.*
import com.tmdbclient.service_tmdb.configuration.ConfigurationFetchWork
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.storage.WorkManager
import javax.inject.Inject
import javax.inject.Singleton
import androidx.work.WorkManager as AndroidWorkManager

/**
 * @author ilya-rb on 03.12.18.
 */
@Singleton
class AppWorkManager @Inject constructor(app: App, workerFactory: WorkerFactory) : WorkManager {

    init {
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()

        AndroidWorkManager.initialize(app.getApplication(), config)
    }

    override fun schedulerPeriodicConfigurationFetch() {
        AndroidWorkManager.getInstance()
            .enqueueUniquePeriodicWork(
                ConfigurationFetchWork::class.java.name,
                ExistingPeriodicWorkPolicy.KEEP,
                ConfigurationFetchWork.createWorkRequest()
            )
    }
}
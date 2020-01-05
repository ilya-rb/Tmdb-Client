package com.illiarb.tmdbclient.work

import android.util.Log
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkerFactory
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.storage.WorkManager
import com.illiarb.tmdblcient.core.storage.WorkManager.WorkType
import javax.inject.Inject
import javax.inject.Singleton
import androidx.work.WorkManager as AndroidWorkManager

/**
 * @author ilya-rb on 03.12.18.
 */
@Singleton
class AppWorkManager @Inject constructor(
    app: App,
    workerFactory: WorkerFactory,
    private val configurationFetchWorker: WorkManager.Worker
) : WorkManager {

    init {
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()

        AndroidWorkManager.initialize(app.getApplication(), config)
    }

    override fun scheduleWork(workType: WorkType) {
        when (workType) {
            WorkType.ConfigurationFetch -> {
                AndroidWorkManager.getInstance()
                    .enqueueUniquePeriodicWork(
                        workType.code,
                        ExistingPeriodicWorkPolicy.KEEP,
                        configurationFetchWorker.workRequestCreator()
                    )
            }
        }
    }
}
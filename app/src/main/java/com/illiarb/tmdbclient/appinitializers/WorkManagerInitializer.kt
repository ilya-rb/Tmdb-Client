package com.illiarb.tmdbclient.appinitializers

import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.illiarb.tmdblcient.core.app.AppInitializer
import com.illiarb.tmdblcient.core.app.App
import javax.inject.Inject

class WorkManagerInitializer @Inject constructor(
    private val workerFactory: WorkerFactory
) : AppInitializer {

    override fun initialize(app: App) {
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()

        WorkManager.initialize(app.getApplication(), config)
    }
}
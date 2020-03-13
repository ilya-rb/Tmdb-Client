package com.illiarb.tmdbclient.appinitializers

import android.util.Log
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager as AndroidWorkManager
import androidx.work.WorkerFactory
import com.illiarb.tmdblcient.core.app.AppInitializer
import com.illiarb.tmdblcient.core.app.App
import com.illiarb.tmdblcient.core.tools.WorkManager
import com.tmdbclient.servicetmdb.configuration.ConfigurationFetchWork
import javax.inject.Inject

class WorkManagerInitializer @Inject constructor(
  private val workerFactory: WorkerFactory,
  private val workManager: WorkManager
) : AppInitializer {

  override fun initialize(app: App) {
    val config = Configuration.Builder()
      .setWorkerFactory(workerFactory)
      .setMinimumLoggingLevel(Log.VERBOSE)
      .build()

    AndroidWorkManager.initialize(app.getApplication(), config)

    workManager.enqueuePeriodicWork(
      ConfigurationFetchWork::class.java.name,
      ExistingPeriodicWorkPolicy.KEEP,
      ConfigurationFetchWork.createWorkRequest()
    )
  }
}
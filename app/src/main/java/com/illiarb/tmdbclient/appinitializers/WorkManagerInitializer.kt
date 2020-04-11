package com.illiarb.tmdbclient.appinitializers

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager as AndroidWorkManager
import androidx.work.WorkerFactory
import com.illiarb.tmdbclient.tools.AppInitializer
import javax.inject.Inject

class WorkManagerInitializer @Inject constructor(
  private val workerFactory: WorkerFactory
  //private val workManager: AndroidWorkManager
) : AppInitializer {

  override fun initialize(app: Application) {
    val config = Configuration.Builder()
      .setWorkerFactory(workerFactory)
      .setMinimumLoggingLevel(Log.VERBOSE)
      .build()

//    AndroidWorkManager.initialize(app, config)

//    workManager.enqueuePeriodicWork(
//      ConfigurationFetchWork::class.java.name,
//      ExistingPeriodicWorkPolicy.KEEP,
//      ConfigurationFetchWork.createWorkRequest()
//    )
  }
}
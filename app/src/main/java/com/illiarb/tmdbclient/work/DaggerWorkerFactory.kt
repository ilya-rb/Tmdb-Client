package com.illiarb.tmdbclient.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.illiarb.tmdbclient.libs.tools.WorkManager
import javax.inject.Inject

class DaggerWorkerFactory @Inject constructor(
  private val configurationFetchWorker: WorkManager.WorkerCreator
) : WorkerFactory() {

  override fun createWorker(
    appContext: Context,
    workerClassName: String,
    workerParameters: WorkerParameters
  ): ListenableWorker? = configurationFetchWorker.createWorkRequest(appContext, workerParameters)
}
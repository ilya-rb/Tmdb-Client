package com.illiarb.tmdbclient.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.illiarb.tmdblcient.core.storage.WorkManager
import javax.inject.Inject

class DaggerWorkerFactory @Inject constructor(
    private val configurationFetchWorker: WorkManager.Worker
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? =
        if (configurationFetchWorker.isWorkerSuited(workerClassName)) {
            configurationFetchWorker.workCreator(appContext, workerParameters)
        } else {
            null
        }
}
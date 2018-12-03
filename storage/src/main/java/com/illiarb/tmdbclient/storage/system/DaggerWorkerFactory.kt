package com.illiarb.tmdbclient.storage.system

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbclient.storage.network.api.config.ConfigurationFetchWork
import com.illiarb.tmdbclient.storage.network.api.service.ConfigurationService
import javax.inject.Inject

/**
 * @author ilya-rb on 03.12.18.
 */
class DaggerWorkerFactory @Inject constructor(
    private val configurationService: ConfigurationService,
    private val persistableStorage: PersistableStorage
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? =
        when (workerClassName) {
            ConfigurationFetchWork::class.java.name -> {
                ConfigurationFetchWork(appContext, workerParameters, configurationService, persistableStorage)
            }
            else -> null
        }
}
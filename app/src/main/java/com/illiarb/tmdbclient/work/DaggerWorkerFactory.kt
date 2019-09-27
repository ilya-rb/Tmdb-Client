package com.illiarb.tmdbclient.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.tmdbclient.service_tmdb.api.ConfigurationApi
import com.tmdbclient.service_tmdb.cache.TmdbCache
import com.tmdbclient.service_tmdb.configuration.ConfigurationFetchWork
import dagger.Lazy
import javax.inject.Inject

class DaggerWorkerFactory @Inject constructor(
    private val configurationFetchWork: Lazy<ConfigurationFetchWork>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? =
        when (workerClassName) {
            ConfigurationFetchWork::class.java.name -> configurationFetchWork.get()
            else -> null
        }
}
package com.illiarb.tmdbclient.appinitializers

import androidx.work.ExistingPeriodicWorkPolicy
import com.illiarb.tmdblcient.core.app.AppInitializer
import com.illiarb.tmdblcient.core.app.App
import com.illiarb.tmdblcient.core.tools.WorkManager
import com.tmdbclient.servicetmdb.configuration.ConfigurationFetchWork
import javax.inject.Inject

class TmdbInitializer @Inject constructor(private val workManager: WorkManager) : AppInitializer {

  override fun initialize(app: App) {
//        workManager.enqueuePeriodicWork(
//            ConfigurationFetchWork::class.java.name,
//            ExistingPeriodicWorkPolicy.KEEP,
//            ConfigurationFetchWork.createWorkRequest()
//        )
  }
}
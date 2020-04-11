package com.illiarb.tmdbclient.libs.tools.internal

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import com.illiarb.tmdbclient.libs.tools.WorkManager
import javax.inject.Inject
import androidx.work.WorkManager as AndroidWorkManager

internal class AppWorkManager @Inject constructor() : WorkManager {

  override fun enqueuePeriodicWork(
    uniqueWorkName: String,
    periodicWorkPolicy: ExistingPeriodicWorkPolicy,
    workRequest: PeriodicWorkRequest
  ) {
    AndroidWorkManager.getInstance()
      .enqueueUniquePeriodicWork(uniqueWorkName, periodicWorkPolicy, workRequest)
  }
}
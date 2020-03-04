package com.illiarb.tmdblcient.core.tools

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest

/**
 * @author ilya-rb on 03.12.18.
 */
interface WorkManager {

  fun enqueuePeriodicWork(
    uniqueWorkName: String,
    periodicWorkPolicy: ExistingPeriodicWorkPolicy,
    workRequest: PeriodicWorkRequest
  )
}
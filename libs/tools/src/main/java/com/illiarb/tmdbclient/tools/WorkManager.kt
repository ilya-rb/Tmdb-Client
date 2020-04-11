package com.illiarb.tmdbclient.tools

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * @author ilya-rb on 03.12.18.
 */
interface WorkManager {

  fun enqueuePeriodicWork(
    uniqueWorkName: String,
    periodicWorkPolicy: ExistingPeriodicWorkPolicy,
    workRequest: PeriodicWorkRequest
  )

  interface WorkerCreator {

    fun createWorkRequest(context: Context, params: WorkerParameters): Worker
  }
}
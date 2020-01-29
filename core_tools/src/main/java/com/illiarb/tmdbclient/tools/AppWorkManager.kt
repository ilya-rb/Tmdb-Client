package com.illiarb.tmdbclient.tools

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import com.illiarb.tmdblcient.core.tools.WorkManager
import javax.inject.Inject
import androidx.work.WorkManager as AndroidWorkManager

class AppWorkManager @Inject constructor() : WorkManager {

    override fun enqueuePeriodicWork(
        uniqueWorkName: String,
        periodicWorkPolicy: ExistingPeriodicWorkPolicy,
        workRequest: PeriodicWorkRequest
    ) {
        AndroidWorkManager.getInstance().enqueueUniquePeriodicWork(uniqueWorkName, periodicWorkPolicy, workRequest)
    }
}
package com.illiarb.tmdblcient.core.storage

import android.content.Context
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * @author ilya-rb on 03.12.18.
 */
typealias WorkerCreator = (Context, WorkerParameters) -> Worker

typealias WorkRequestCreator = () -> PeriodicWorkRequest

interface WorkManager {

    fun scheduleWork(workType: WorkType)

    enum class WorkType(val code: String) {
        ConfigurationFetch("configuration_fetch")
    }

    interface Worker {

        val workCreator: WorkerCreator

        val workRequestCreator: WorkRequestCreator

        fun isWorkerSuited(workerClassName: String): Boolean
    }
}
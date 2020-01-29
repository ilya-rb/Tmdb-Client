package com.illiarb.tmdblcient.core.tools

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

interface WorkerCreator {

    fun createWorkRequest(context: Context, params: WorkerParameters): Worker
}
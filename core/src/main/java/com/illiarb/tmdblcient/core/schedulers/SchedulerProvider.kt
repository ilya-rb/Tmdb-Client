package com.illiarb.tmdblcient.core.schedulers

import io.reactivex.Scheduler

interface SchedulerProvider {

    fun provideIoScheduler(): Scheduler

    fun provideComputationScheduler(): Scheduler

    fun provideNewThreadScheduler(): Scheduler

    fun provideMainThreadScheduler(): Scheduler

}
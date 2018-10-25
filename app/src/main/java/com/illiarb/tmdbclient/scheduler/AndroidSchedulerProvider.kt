package com.illiarb.tmdbclient.scheduler

import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AndroidSchedulerProvider : SchedulerProvider {

    override fun provideIoScheduler(): Scheduler = Schedulers.io()

    override fun provideComputationScheduler(): Scheduler = Schedulers.computation()

    override fun provideNewThreadScheduler(): Scheduler = Schedulers.newThread()

    override fun provideMainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()
}
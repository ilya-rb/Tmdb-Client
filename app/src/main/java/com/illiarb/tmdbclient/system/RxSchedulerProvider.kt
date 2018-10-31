package com.illiarb.tmdbclient.system

import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RxSchedulerProvider : SchedulerProvider {

    override fun provideIoScheduler(): Scheduler = Schedulers.io()

    override fun provideComputationScheduler(): Scheduler = Schedulers.computation()

    override fun provideNewThreadScheduler(): Scheduler = Schedulers.newThread()

    override fun provideMainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()
}
package com.illiarb.tmdbclient.tools

import com.illiarb.tmdblcient.core.system.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author ilya-rb on 04.11.18.
 */
class RxSchedulerProvider @Inject constructor() : SchedulerProvider {

    override fun provideIoScheduler(): Scheduler = Schedulers.io()

    override fun provideComputationScheduler(): Scheduler = Schedulers.computation()

    override fun provideNewThreadScheduler(): Scheduler = Schedulers.newThread()

    override fun provideMainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()
}
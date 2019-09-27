package com.illiarb.tmdbclient.di.modules

import androidx.work.WorkerFactory
import com.illiarb.tmdbclient.work.AppWorkManager
import com.illiarb.tmdbclient.work.DaggerWorkerFactory
import com.illiarb.tmdblcient.core.storage.WorkManager
import dagger.Binds
import dagger.Module

@Module
interface WorkModule {

    @Binds
    fun bindWorkerFactory(factory: DaggerWorkerFactory): WorkerFactory

    @Binds
    fun bindWorkManager(manager: AppWorkManager): WorkManager
}
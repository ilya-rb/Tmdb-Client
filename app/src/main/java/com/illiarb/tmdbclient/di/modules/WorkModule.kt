package com.illiarb.tmdbclient.di.modules

import androidx.work.WorkerFactory
import com.illiarb.tmdbclient.work.DaggerWorkerFactory
import dagger.Binds
import dagger.Module

@Module
interface WorkModule {

    @Binds
    fun bindWorkerFactory(factory: DaggerWorkerFactory): WorkerFactory
}
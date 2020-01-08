package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.storage.WorkManager

interface TmdbProvider {

    fun provideConfigurationFetchWorker(): WorkManager.Worker
}
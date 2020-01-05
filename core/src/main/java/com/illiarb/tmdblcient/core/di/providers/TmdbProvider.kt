package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.storage.WorkManager

interface TmdbProvider {

    fun provideTmdbService(): TmdbService

    fun provideConfigurationFetchWorker(): WorkManager.Worker
}
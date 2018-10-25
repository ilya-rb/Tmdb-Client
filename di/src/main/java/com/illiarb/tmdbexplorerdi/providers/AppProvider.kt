package com.illiarb.tmdbexplorerdi.providers

import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdblcient.core.system.ResourceResolver
import com.illiarb.tmdblcient.core.system.SchedulerProvider

interface AppProvider : InteractorsProvider, StorageProvider {

    fun provideApp(): App

    fun provideSchedulerProvider(): SchedulerProvider

    fun provideResourceResolver(): ResourceResolver
}
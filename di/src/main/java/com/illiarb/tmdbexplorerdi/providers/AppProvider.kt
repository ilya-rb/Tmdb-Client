package com.illiarb.tmdbexplorerdi.providers

import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdblcient.core.resources.ResourceResolver
import com.illiarb.tmdblcient.core.schedulers.SchedulerProvider

interface AppProvider : InteractorsProvider, NetworkProvider {

    fun provideApp(): App

    fun provideSchedulerProvider(): SchedulerProvider

    fun provideResourceResolver(): ResourceResolver
}
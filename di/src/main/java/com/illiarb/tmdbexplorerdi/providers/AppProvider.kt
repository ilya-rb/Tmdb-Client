package com.illiarb.tmdbexplorerdi.providers

import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.system.EventBus
import com.illiarb.tmdblcient.core.system.SchedulerProvider

interface AppProvider : InteractorsProvider, StorageProvider {

    fun getApp(): App

    fun provideSchedulerProvider(): SchedulerProvider

    fun provideNavigator(): Navigator

    fun provideEventBus(): EventBus

    fun provideNavigationGraphRes(): Int
}
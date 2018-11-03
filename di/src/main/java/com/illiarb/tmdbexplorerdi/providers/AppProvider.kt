package com.illiarb.tmdbexplorerdi.providers

import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdblcient.core.navigation.Navigator

interface AppProvider : InteractorsProvider, StorageProvider, ToolsProvider {

    fun getApp(): App

    fun provideNavigator(): Navigator

    fun provideNavigationGraphRes(): Int
}
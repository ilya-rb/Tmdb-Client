package com.illiarb.tmdbexplorerdi.providers

import com.illiarb.tmdbexplorerdi.App

interface AppProvider : InteractorsProvider, StorageProvider, ToolsProvider {

    fun getApp(): App
}
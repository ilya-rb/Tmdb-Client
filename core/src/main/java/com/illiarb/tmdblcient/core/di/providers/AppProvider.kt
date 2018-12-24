package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.di.App

/**
 * @author ilya-rb on 24.12.18.
 */
interface AppProvider : InteractorsProvider, StorageProvider, ToolsProvider {
    fun getApp(): App
}
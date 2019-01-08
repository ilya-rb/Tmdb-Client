package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.system.feature.FeatureInstaller

/**
 * @author ilya-rb on 24.12.18.
 */
interface AppProvider : UseCaseProvider, StorageProvider, ToolsProvider {

    fun getApp(): App

    fun getFeatureInstaller(): FeatureInstaller
}
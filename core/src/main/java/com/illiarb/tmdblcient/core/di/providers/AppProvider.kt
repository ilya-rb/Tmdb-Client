package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.system.dynamicfeature.FeatureInstaller
import com.illiarb.tmdblcient.core.system.featureconfig.FeatureConfig

/**
 * @author ilya-rb on 24.12.18.
 */
interface AppProvider : StorageProvider, ToolsProvider, InteractorsProvider {

    fun getApp(): App

    fun getFeatureInstaller(): FeatureInstaller

    fun getFeatureConfig(): FeatureConfig

    fun router(): Router

    fun navigatorHolder(): NavigatorHolder
}
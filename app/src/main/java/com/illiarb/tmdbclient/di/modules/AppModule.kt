package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.system.RxFeatureInstaller
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.system.ResourceResolver
import com.illiarb.tmdblcient.core.system.feature.FeatureInstaller
import dagger.Module
import dagger.Provides

/**
 * @author ilya-rb on 03.11.18.
 */
@Module
class AppModule(val app: App) {

    @Provides
    fun provideApp(): App = app

    @Provides
    fun provideFeatureInstaller(resourceResolver: ResourceResolver): FeatureInstaller =
        RxFeatureInstaller(app, resourceResolver)
}
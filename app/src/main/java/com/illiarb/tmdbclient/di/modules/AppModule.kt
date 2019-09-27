package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.navigation.AppFeatureInstaller
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.feature.dynamic.FeatureInstaller
import com.illiarb.tmdblcient.core.storage.ResourceResolver
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
        AppFeatureInstaller(app, resourceResolver)
}
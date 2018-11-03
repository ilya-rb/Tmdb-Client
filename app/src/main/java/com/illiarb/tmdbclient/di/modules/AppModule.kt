package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.navigation.AppNavigator
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdblcient.core.navigation.Navigator
import com.illiarb.tmdblcient.core.system.EventBus
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author ilya-rb on 03.11.18.
 */
@Module
class AppModule(val app: App) {

    @Provides
    fun provideApp(): App = app

    @Provides
    @Singleton
    fun provideNavigator(eventBus: EventBus): Navigator = AppNavigator(eventBus)

    @Provides
    fun provideAppNavigationGraphRes(): Int = R.navigation.app_nav_graph
}
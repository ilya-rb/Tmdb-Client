package com.illiarb.tmdbclient.tools.di.modules

import com.illiarb.tmdbclient.tools.navigation.AppRouter
import com.illiarb.tmdbclient.tools.navigation.SimpleNavigatorHolder
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.navigation.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author ilya-rb on 24.12.18.
 */
@Module
class NavigationModule {

    @Provides
    @Singleton
    fun provideRouter(navigatorHolder: SimpleNavigatorHolder): Router = AppRouter(navigatorHolder)

    // Inner provides
    // for core implementation
    @Provides
    @Singleton
    fun provideSimpleNavigatorHolder() = SimpleNavigatorHolder()

    // Outer provides
    // for application
    @Provides
    fun provideNavigatorHolder(navigatorHolder: SimpleNavigatorHolder): NavigatorHolder = navigatorHolder
}
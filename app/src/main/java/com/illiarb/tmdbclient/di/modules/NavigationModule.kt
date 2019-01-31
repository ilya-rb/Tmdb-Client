package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.navigation.AppNavigatorHolder
import com.illiarb.tmdbclient.navigation.AppRouter
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.navigation.Router
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 31.01.19.
 */
@Module
interface NavigationModule {

    @Binds
    fun bindNavigatorHolder(impl: AppNavigatorHolder): NavigatorHolder

    @Binds
    fun bindRouter(impl: AppRouter): Router
}
package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.navigation.NavigatorHolder
import com.illiarb.tmdbclient.navigation.Router
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 31.01.19.
 */
@Module
interface NavigationModule {

  @Binds
  fun bindRouter(router: Router.DefaultRouter): Router

  @Binds
  fun bindNavigatorHolder(holder: NavigatorHolder.ActionsBuffer): NavigatorHolder
}
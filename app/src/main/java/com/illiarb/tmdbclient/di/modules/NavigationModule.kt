package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.navigation.ActionsBuffer
import com.illiarb.tmdbclient.navigation.AppRouter
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
  fun bindRouter(router: AppRouter): Router

  @Binds
  fun bindNavigatorHolder(holder: ActionsBuffer): NavigatorHolder
}
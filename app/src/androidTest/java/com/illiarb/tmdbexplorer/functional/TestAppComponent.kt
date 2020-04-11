package com.illiarb.tmdbexplorer.functional

import com.illiarb.tmdbclient.MobileApplication
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdbclient.navigation.ActionsBuffer
import com.illiarb.tmdbclient.navigation.AppRouter
import com.illiarb.tmdbclient.navigation.NavigatorHolder
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.tools.di.ToolsProvider
import com.illiarb.tmdbcliient.coretest.TestDependencies
import com.illiarb.tmdclient.analytics.di.AnalyticsProvider
import com.tmdbclient.servicetmdb.di.TmdbProvider

class TestAppComponent : AppComponent,
  ToolsProvider by TestDependencies,
  TmdbProvider by TestDependencies,
  AnalyticsProvider by TestDependencies {

  private val navigatorHolder = ActionsBuffer()
  private val router = AppRouter(navigatorHolder)

  override fun navigatorHolder(): NavigatorHolder = navigatorHolder

  override fun router(): Router = router

  override fun inject(app: MobileApplication) {
    app.appInitializers = emptySet()
  }
}
package com.illiarb.tmdbclient.functional

import android.app.Application
import com.illiarb.tmdbclient.App
import com.illiarb.tmdbclient.AppBuildConfig
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdbclient.libs.buildconfig.BuildConfig
import com.illiarb.tmdbclient.libs.test.TestDependencies
import com.illiarb.tmdbclient.libs.tools.di.ToolsProvider
import com.illiarb.tmdbclient.navigation.NavigatorHolder
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.services.analytics.di.AnalyticsProvider
import com.illiarb.tmdbclient.services.tmdb.di.TmdbProvider

class TestAppComponent(private val app: Application) : AppComponent,
  ToolsProvider by TestDependencies,
  TmdbProvider by TestDependencies,
  AnalyticsProvider by TestDependencies {

  private val navigatorHolder = NavigatorHolder.ActionsBuffer()
  private val router = Router.DefaultRouter(navigatorHolder)

  override fun navigatorHolder(): NavigatorHolder = navigatorHolder

  override fun router(): Router = router

  override fun inject(app: App) = Unit

  override fun buildConfig(): BuildConfig = AppBuildConfig(app)
}
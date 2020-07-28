package com.illiarb.tmdbclient

import android.app.Application
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdbclient.di.AppInjector
import com.illiarb.tmdbclient.di.DaggerAppComponent
import com.illiarb.tmdbclient.libs.buildconfig.BuildConfig
import com.illiarb.tmdbclient.libs.buildconfig.TmdbConfig
import com.illiarb.tmdbclient.libs.tools.AppInitializer
import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.libs.tools.di.DaggerToolsComponent
import com.illiarb.tmdbclient.services.analytics.di.AnalyticsComponent
import com.illiarb.tmdbclient.services.analytics.di.DaggerAnalyticsComponent
import com.illiarb.tmdbclient.services.tmdb.di.DaggerTmdbComponent
import com.illiarb.tmdbclient.services.tmdb.di.TmdbComponent
import javax.inject.Inject

class App : Application() {

  private val appBuildConfig = AppBuildConfig(context = this)
  private val appTmdbConfig = AppTmdbConfig()

  private val appComponent: AppComponent by lazy {
    val toolsComponent = DaggerToolsComponent.factory().create(app = this)
    val analyticsComponent = DaggerAnalyticsComponent.factory()
      .create(
        app = this,
        dependencies = object : AnalyticsComponent.Dependencies {
          override fun buildConfig(): BuildConfig = appBuildConfig
        }
      )

    val tmdbComponent = DaggerTmdbComponent.factory()
      .create(
        app = this,
        dependencies = object : TmdbComponent.Dependencies {
          override fun buildConfig(): BuildConfig = appBuildConfig
          override fun tmdbConfig(): TmdbConfig = appTmdbConfig
          override fun resourceResolver(): ResourceResolver = toolsComponent.resourceResolver()
          override fun dispatcherProvider(): DispatcherProvider =
            toolsComponent.dispatcherProvider()
        }
      )

    DaggerAppComponent.factory().create(
      app = this,
      analyticsProvider = analyticsComponent,
      toolsProvider = toolsComponent,
      tmdbProvider = tmdbComponent
    )
  }

  override fun onCreate() {
    super.onCreate()

    AppInjector(application = this, provider = appComponent).registerLifecycleCallbacks()

    appComponent.inject(app = this)
  }

  @Inject
  @Suppress("unused", "ProtectedInFinal")
  protected fun runInitializers(initializers: Set<@JvmSuppressWildcards AppInitializer>) {
    initializers.forEach {
      it.initialize(app = this)
    }
  }
}
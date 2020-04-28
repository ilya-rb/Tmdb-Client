package com.illiarb.tmdbclient

import android.app.Application
import com.google.firebase.FirebaseApp
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

  private val appBuildConfig = AppBuildConfig(this)
  private val appTmdbConfig = AppTmdbConfig()

  private val appComponent: AppComponent by lazy {
    val toolsComponent = DaggerToolsComponent.builder()
      .application(this)
      .build()

    val analyticsComponent = DaggerAnalyticsComponent.builder()
      .application(this)
      .dependencies(
        object : AnalyticsComponent.Dependencies {
          override fun buildConfig(): BuildConfig = appBuildConfig
        }
      )
      .build()

    val tmdbComponent = DaggerTmdbComponent.builder()
      .application(this)
      .dependencies(
        object : TmdbComponent.Dependencies {
          override fun buildConfig(): BuildConfig = appBuildConfig
          override fun tmdbConfig(): TmdbConfig = appTmdbConfig
          override fun resourceResolver(): ResourceResolver = toolsComponent.resourceResolver()
          override fun dispatcherProvider(): DispatcherProvider =
            toolsComponent.dispatcherProvider()
        }
      )
      .build()

    DaggerAppComponent.builder()
      .application(this)
      .analyticsProvider(analyticsComponent)
      .toolsProvider(toolsComponent)
      .tmdbProvider(tmdbComponent)
      .build()
  }

  override fun onCreate() {
    super.onCreate()

    AppInjector(this, appComponent).registerLifecycleCallbacks()

    FirebaseApp.initializeApp(this)

    appComponent.inject(this)
  }

  @Inject
  @Suppress("unused", "ProtectedInFinal")
  protected fun runInitializers(initializers: Set<@JvmSuppressWildcards AppInitializer>) {
    initializers.forEach {
      it.initialize(this)
    }
  }
}
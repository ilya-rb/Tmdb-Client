package com.illiarb.tmdbclient

import android.app.Application
import com.google.firebase.FirebaseApp
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdbclient.di.AppInjector
import com.illiarb.tmdbclient.di.DaggerAppComponent
import com.illiarb.tmdbclient.libs.tools.AppInitializer
import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.libs.tools.di.DaggerToolsComponent
import com.illiarb.tmdbclient.services.analytics.di.DaggerAnalyticsComponent
import com.illiarb.tmdbclient.services.tmdb.di.DaggerTmdbComponent
import com.illiarb.tmdbclient.services.tmdb.di.TmdbComponent
import javax.inject.Inject

// TODO: Remove open
open class MobileApplication : Application() {

  @Inject
  lateinit var appInitializers: Set<@JvmSuppressWildcards AppInitializer>

  open val appComponent: AppComponent by lazy {
    val toolsComponent = DaggerToolsComponent.builder()
      .application(this)
      .build()

    val analyticsComponent = DaggerAnalyticsComponent.builder()
      .application(this)
      .build()

    val tmdbComponent = DaggerTmdbComponent.builder()
      .application(this)
      .dependencies(
        object : TmdbComponent.Dependencies {
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

    appComponent.inject(this)

    AppInjector(this).registerLifecycleCallbacks()

    FirebaseApp.initializeApp(this)

    appInitializers.forEach {
      it.initialize(this)
    }
  }
}
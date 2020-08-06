package com.illiarb.tmdbclient

import android.app.Application
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdbclient.di.AppInjector
import com.illiarb.tmdbclient.di.DaggerAppComponent
import com.illiarb.tmdbclient.libs.tools.AppInitializer
import javax.inject.Inject

class App : Application() {

  private val appComponent: AppComponent by lazy {
    DaggerAppComponent.factory().create(app = this)
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
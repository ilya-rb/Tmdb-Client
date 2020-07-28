package com.illiarb.tmdbclient.services.analytics.di

import android.app.Application
import com.illiarb.tmdbclient.libs.buildconfig.BuildConfig
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * @author ilya-rb on 20.02.19.
 */
@Component(
  modules = [AnalyticsModule::class],
  dependencies = [AnalyticsComponent.Dependencies::class]
)
@Singleton
interface AnalyticsComponent : AnalyticsProvider {

  interface Dependencies {
    fun buildConfig(): BuildConfig
  }

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance app: Application, dependencies: Dependencies): AnalyticsComponent
  }
}

interface AnalyticsProvider {
  fun analyticsService(): AnalyticsService
}
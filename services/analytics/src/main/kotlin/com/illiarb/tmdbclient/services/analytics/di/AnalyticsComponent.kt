package com.illiarb.tmdbclient.services.analytics.di

import android.app.Application
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * @author ilya-rb on 20.02.19.
 */
@Component(modules = [AnalyticsModule::class])
@Singleton
interface AnalyticsComponent : AnalyticsProvider {

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(app: Application): Builder
    fun build(): AnalyticsComponent
  }
}

interface AnalyticsProvider {
  fun analyticsService(): AnalyticsService
}
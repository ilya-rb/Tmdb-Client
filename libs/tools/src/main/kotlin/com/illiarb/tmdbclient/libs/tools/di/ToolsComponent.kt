package com.illiarb.tmdbclient.libs.tools.di

import android.app.Application
import com.illiarb.tmdbclient.libs.tools.ConnectivityStatus
import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.libs.tools.FeatureFlagStore
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.libs.tools.WorkManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ToolsModule::class])
@Singleton
interface ToolsComponent : ToolsProvider {

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(app: Application): Builder
    fun build(): ToolsComponent
  }
}

interface ToolsProvider {
  fun connectivityStatus(): ConnectivityStatus
  fun resourceResolver(): ResourceResolver
  fun dispatcherProvider(): DispatcherProvider
  fun featureFlagStore(): FeatureFlagStore
  fun workManager(): WorkManager
}
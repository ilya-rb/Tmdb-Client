package com.illiarb.tmdbclient.libs.tools.di

import android.app.Application
import com.illiarb.tmdbclient.libs.tools.ConnectivityStatus
import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.libs.tools.FeatureFlagStore
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ToolsModule::class])
@Singleton
interface ToolsComponent : ToolsProvider {

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance app: Application): ToolsComponent
  }
}

interface ToolsProvider {
  fun connectivityStatus(): ConnectivityStatus
  fun resourceResolver(): ResourceResolver
  fun dispatcherProvider(): DispatcherProvider
  fun featureFlagStore(): FeatureFlagStore
}
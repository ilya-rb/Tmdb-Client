package com.illiarb.tmdbclient.libs.tools.di

import com.illiarb.tmdbclient.libs.tools.ConnectivityStatus
import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.libs.tools.FeatureFlagStore
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.libs.tools.internal.AndroidConnectivityStatus
import com.illiarb.tmdbclient.libs.tools.internal.AndroidResourceResolver
import com.illiarb.tmdbclient.libs.tools.internal.CoroutineDispatcherProvider
import com.illiarb.tmdbclient.libs.tools.internal.FirebaseFeatureFlagStore
import dagger.Binds
import dagger.Module

@Module
abstract class ToolsModule {

  @Binds
  internal abstract fun bindConnectivityStatus(
      connectivityStatus: AndroidConnectivityStatus
  ): ConnectivityStatus

  @Binds
  internal abstract fun bindDispatcherProvider(
      dispatcherProvider: CoroutineDispatcherProvider
  ): DispatcherProvider

  @Binds
  internal abstract fun bindResourceResolver(
      resourceResolver: AndroidResourceResolver
  ): ResourceResolver

  @Binds
  internal abstract fun bindFeatureFlagStore(
      featureFlagStore: FirebaseFeatureFlagStore
  ): FeatureFlagStore
}
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
internal interface ToolsModule {

  @Binds
  fun bindConnectivityStatus(connectivityStatus: AndroidConnectivityStatus): ConnectivityStatus

  @Binds
  fun bindDispatcherProvider(dispatcherProvider: CoroutineDispatcherProvider): DispatcherProvider

  @Binds
  fun bindResourceResolver(resourceResolver: AndroidResourceResolver): ResourceResolver

  @Binds
  fun bindFeatureFlagStore(featureFlagStore: FirebaseFeatureFlagStore): FeatureFlagStore
}
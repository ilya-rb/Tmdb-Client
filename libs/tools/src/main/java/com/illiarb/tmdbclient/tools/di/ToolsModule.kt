package com.illiarb.tmdbclient.tools.di

import com.illiarb.tmdbclient.tools.ConnectivityStatus
import com.illiarb.tmdbclient.tools.DispatcherProvider
import com.illiarb.tmdbclient.tools.FeatureFlagStore
import com.illiarb.tmdbclient.tools.ResourceResolver
import com.illiarb.tmdbclient.tools.WorkManager
import com.illiarb.tmdbclient.tools.internal.AndroidConnectivityStatus
import com.illiarb.tmdbclient.tools.internal.AndroidResourceResolver
import com.illiarb.tmdbclient.tools.internal.AppWorkManager
import com.illiarb.tmdbclient.tools.internal.CoroutineDispatcherProvider
import com.illiarb.tmdbclient.tools.internal.FirebaseFeatureFlagStore
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

  @Binds
  fun bindWorkManager(workManager: AppWorkManager): WorkManager
}
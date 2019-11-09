package com.illiarb.tmdbcliient.core_test

import com.illiarb.tmdbcliient.core_test.analytics.TestAnalyticsService
import com.illiarb.tmdbcliient.core_test.navigation.TestRouter
import com.illiarb.tmdbcliient.core_test.storage.TestFeatureFlagStore
import com.illiarb.tmdbcliient.core_test.storage.TestResourceResolver
import com.illiarb.tmdbcliient.core_test.tmdb.TestTmdbService
import com.illiarb.tmdbcliient.core_test.tools.TestConnectivityStatus
import com.illiarb.tmdbcliient.core_test.tools.TestDispatcherProvider
import com.illiarb.tmdblcient.core.di.providers.AnalyticsProvider
import com.illiarb.tmdblcient.core.di.providers.StorageProvider
import com.illiarb.tmdblcient.core.di.providers.TmdbProvider
import com.illiarb.tmdblcient.core.di.providers.ToolsProvider
import com.illiarb.tmdblcient.core.feature.FeatureFlagStore
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.services.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus
import com.illiarb.tmdblcient.core.tools.DispatcherProvider

object TestDependencyProvider :
    StorageProvider,
    ToolsProvider,
    AnalyticsProvider,
    TmdbProvider {

    val router: Router
        get() = TestRouter()

    override fun provideTmdbService(): TmdbService = TestTmdbService()

    override fun provideResourceResolver(): ResourceResolver = TestResourceResolver()

    override fun provideFeatureFlagStore(): FeatureFlagStore = TestFeatureFlagStore()

    override fun provideDispatcherProvider(): DispatcherProvider = TestDispatcherProvider()

    override fun provideConnectivityStatus(): ConnectivityStatus = TestConnectivityStatus()

    override fun provideAnalyticsService(): AnalyticsService = TestAnalyticsService()
}
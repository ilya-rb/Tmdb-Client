package com.illiarb.tmdbcliient.coretest

import com.illiarb.tmdbcliient.coretest.analytics.TestAnalyticsService
import com.illiarb.tmdbcliient.coretest.navigation.TestRouter
import com.illiarb.tmdbcliient.coretest.storage.TestFeatureFlagStore
import com.illiarb.tmdbcliient.coretest.storage.TestResourceResolver
import com.illiarb.tmdbcliient.coretest.tmdb.TestTmdbService
import com.illiarb.tmdbcliient.coretest.tools.TestConnectivityStatus
import com.illiarb.tmdbcliient.coretest.tools.TestDispatcherProvider
import com.illiarb.tmdblcient.core.di.providers.AnalyticsProvider
import com.illiarb.tmdblcient.core.di.providers.StorageProvider
import com.illiarb.tmdblcient.core.di.providers.TmdbProvider
import com.illiarb.tmdblcient.core.di.providers.ToolsProvider
import com.illiarb.tmdblcient.core.feature.FeatureFlagStore
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.services.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import com.illiarb.tmdblcient.core.storage.WorkManager
import com.illiarb.tmdblcient.core.storage.WorkRequestCreator
import com.illiarb.tmdblcient.core.storage.WorkerCreator
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus
import com.illiarb.tmdblcient.core.tools.DispatcherProvider

object TestDependencyProvider : StorageProvider, ToolsProvider, AnalyticsProvider, TmdbProvider {

    val router: Router
        get() = TestRouter()

    override fun provideTmdbService(): TmdbService = TestTmdbService()

    override fun provideResourceResolver(): ResourceResolver = TestResourceResolver()

    override fun provideFeatureFlagStore(): FeatureFlagStore = TestFeatureFlagStore()

    override fun provideDispatcherProvider(): DispatcherProvider = TestDispatcherProvider()

    override fun provideConnectivityStatus(): ConnectivityStatus = TestConnectivityStatus()

    override fun provideAnalyticsService(): AnalyticsService = TestAnalyticsService()

    override fun provideConfigurationFetchWorker(): WorkManager.Worker {
        return object : WorkManager.Worker {
            override fun isWorkerSuitable(workerClassName: String): Boolean = false
            override val workCreator: WorkerCreator get() = { _, _ -> TODO() }
            override val workRequestCreator: WorkRequestCreator get() = { TODO() }
        }
    }
}
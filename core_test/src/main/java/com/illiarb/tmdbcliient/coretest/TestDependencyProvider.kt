package com.illiarb.tmdbcliient.coretest

import com.illiarb.tmdbcliient.coretest.analytics.TestAnalyticsService
import com.illiarb.tmdbcliient.coretest.interactor.TestGenresInteractor
import com.illiarb.tmdbcliient.coretest.interactor.TestHomeInteractor
import com.illiarb.tmdbcliient.coretest.interactor.TestMoviesInteractor
import com.illiarb.tmdbcliient.coretest.interactor.TestTrendingInteractor
import com.illiarb.tmdbcliient.coretest.navigation.TestRouter
import com.illiarb.tmdbcliient.coretest.repository.TestGenresRepository
import com.illiarb.tmdbcliient.coretest.repository.TestMovieRepository
import com.illiarb.tmdbcliient.coretest.storage.TestFeatureFlagStore
import com.illiarb.tmdbcliient.coretest.storage.TestResourceResolver
import com.illiarb.tmdbcliient.coretest.tools.TestConnectivityStatus
import com.illiarb.tmdbcliient.coretest.tools.TestDispatcherProvider
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.di.providers.AnalyticsProvider
import com.illiarb.tmdblcient.core.di.providers.InteractorsProvider
import com.illiarb.tmdblcient.core.di.providers.StorageProvider
import com.illiarb.tmdblcient.core.di.providers.TmdbProvider
import com.illiarb.tmdblcient.core.di.providers.ToolsProvider
import com.illiarb.tmdblcient.core.feature.FeatureFlagStore
import com.illiarb.tmdblcient.core.interactor.GenresInteractor
import com.illiarb.tmdblcient.core.interactor.HomeInteractor
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.interactor.TrendingInteractor
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import com.illiarb.tmdblcient.core.storage.WorkManager
import com.illiarb.tmdblcient.core.storage.WorkRequestCreator
import com.illiarb.tmdblcient.core.storage.WorkerCreator
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import com.tmdbclient.servicetmdb.repository.GenresRepository
import com.tmdbclient.servicetmdb.repository.MoviesRepository

object TestDependencyProvider : StorageProvider,
    ToolsProvider,
    AnalyticsProvider,
    TmdbProvider,
    InteractorsProvider {

    fun provideMovieRepository(): MoviesRepository = TestMovieRepository()

    fun provideGenresRepository(): GenresRepository = TestGenresRepository()

    val router: Router
        get() = TestRouter()

    override fun provideMoviesInteractor(): MoviesInteractor =
        TestMoviesInteractor(provideMovieRepository())

    override fun provideGenresInteractor(): GenresInteractor =
        TestGenresInteractor(provideGenresRepository())

    override fun provideHomeInteractor(): HomeInteractor =
        TestHomeInteractor()

    override fun provideTrendingInteractor(): TrendingInteractor =
        TestTrendingInteractor()

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
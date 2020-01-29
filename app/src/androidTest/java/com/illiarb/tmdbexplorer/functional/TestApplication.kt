package com.illiarb.tmdbexplorer.functional

import android.app.Application
import com.illiarb.tmdbclient.MobileAppInjector
import com.illiarb.tmdbclient.navigation.ActionsBuffer
import com.illiarb.tmdbclient.navigation.AppRouter
import com.illiarb.tmdbclient.storage.di.StorageComponent
import com.illiarb.tmdbclient.tools.di.ToolsComponent
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.storage.FeatureFlagStore
import com.illiarb.tmdblcient.core.interactor.GenresInteractor
import com.illiarb.tmdblcient.core.interactor.HomeInteractor
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.interactor.TrendingInteractor
import com.illiarb.tmdblcient.core.navigation.NavigatorHolder
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import com.illiarb.tmdblcient.core.tools.WorkManager
import com.illiarb.tmdblcient.core.storage.WorkRequestCreator
import com.illiarb.tmdblcient.core.storage.WorkerCreator
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import com.illiarb.tmdclient.analytics.di.AnalyticsComponent
import com.tmdbclient.servicetmdb.di.TmdbComponent

class TestApplication : Application(), App {

    private val navigatorHolder = ActionsBuffer()
    private val router = AppRouter(navigatorHolder)
    private val provider by lazy {
        createAppProvider()
    }

    override fun onCreate() {
        super.onCreate()
        MobileAppInjector(this).registerLifecycleCallbacks()
    }

    override fun getApplication(): Application = this

    override fun getAppProvider(): AppProvider = provider

    private fun createAppProvider(): AppProvider {
        val toolsProvider = ToolsComponent.get(this)
        val storageProvider = StorageComponent.get(this, toolsProvider)
        val analyticsProvider = AnalyticsComponent.get(this)
        val tmdbProvider = TmdbComponent.get(this, toolsProvider, storageProvider)

        return object : AppProvider {
            override fun getApp(): App = this@TestApplication
            override fun router(): Router = router
            override fun navigatorHolder(): NavigatorHolder = navigatorHolder
            override fun provideResourceResolver(): ResourceResolver =
                storageProvider.provideResourceResolver()

            override fun provideFeatureFlagStore(): FeatureFlagStore =
                storageProvider.provideFeatureFlagStore()

            override fun provideDispatcherProvider(): DispatcherProvider =
                toolsProvider.provideDispatcherProvider()

            override fun provideConnectivityStatus(): ConnectivityStatus =
                toolsProvider.provideConnectivityStatus()

            override fun provideAnalyticsService(): AnalyticsService =
                analyticsProvider.provideAnalyticsService()

            override fun provideConfigurationFetchWorker(): WorkManager.Worker =
                createConfigWorker()

            override fun provideMoviesInteractor(): MoviesInteractor = tmdbProvider.provideMoviesInteractor()
            override fun provideGenresInteractor(): GenresInteractor = tmdbProvider.provideGenresInteractor()
            override fun provideHomeInteractor(): HomeInteractor = tmdbProvider.provideHomeInteractor()
            override fun provideTrendingInteractor(): TrendingInteractor =
                tmdbProvider.provideTrendingInteractor()
        }
    }

    private fun createConfigWorker(): WorkManager.Worker {
        return object : WorkManager.Worker {

            override val workCreator: WorkerCreator
                get() = TODO("not implemented")

            override val workRequestCreator: WorkRequestCreator
                get() = TODO("not implemented")

            override fun isWorkerSuitable(workerClassName: String): Boolean = false
        }
    }
}
package com.illiarb.tmdbcliient.coretest

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.illiarb.tmdbclient.tools.ConnectivityStatus
import com.illiarb.tmdbclient.tools.DateFormatter
import com.illiarb.tmdbclient.tools.DispatcherProvider
import com.illiarb.tmdbclient.tools.FeatureFlagStore
import com.illiarb.tmdbclient.tools.ResourceResolver
import com.illiarb.tmdbclient.tools.WorkManager
import com.illiarb.tmdbclient.tools.di.ToolsProvider
import com.illiarb.tmdbcliient.coretest.analytics.TestAnalyticsService
import com.illiarb.tmdbcliient.coretest.interactor.TestGenresInteractor
import com.illiarb.tmdbcliient.coretest.interactor.TestHomeInteractor
import com.illiarb.tmdbcliient.coretest.interactor.TestMoviesInteractor
import com.illiarb.tmdbcliient.coretest.interactor.TestTrendingInteractor
import com.illiarb.tmdbcliient.coretest.tools.TestConnectivityStatus
import com.illiarb.tmdbcliient.coretest.tools.TestDispatcherProvider
import com.illiarb.tmdbcliient.coretest.tools.TestFeatureFlagStore
import com.illiarb.tmdbcliient.coretest.tools.TestResourceResolver
import com.illiarb.tmdclient.analytics.AnalyticsService
import com.illiarb.tmdclient.analytics.di.AnalyticsProvider
import com.tmdbclient.servicetmdb.di.TmdbProvider
import com.tmdbclient.servicetmdb.interactor.GenresInteractor
import com.tmdbclient.servicetmdb.interactor.HomeInteractor
import com.tmdbclient.servicetmdb.interactor.MoviesInteractor
import com.tmdbclient.servicetmdb.interactor.TrendingInteractor

@Suppress("TooManyFunctions")
object TestDependencies : ToolsProvider, AnalyticsProvider, TmdbProvider {

  override fun connectivityStatus(): ConnectivityStatus = TestConnectivityStatus()
  override fun resourceResolver(): ResourceResolver = TestResourceResolver()
  override fun dispatcherProvider(): DispatcherProvider = TestDispatcherProvider()
  override fun featureFlagStore(): FeatureFlagStore = TestFeatureFlagStore()

  override fun workManager(): WorkManager =
    object : WorkManager {
      override fun enqueuePeriodicWork(
        uniqueWorkName: String,
        periodicWorkPolicy: ExistingPeriodicWorkPolicy,
        workRequest: PeriodicWorkRequest
      ) = Unit
    }

  override fun analyticsService(): AnalyticsService = TestAnalyticsService()
  override fun homeInteractor(): HomeInteractor = TestHomeInteractor()
  override fun genresInteractor(): GenresInteractor = TestGenresInteractor()
  override fun moviesInteractor(): MoviesInteractor = TestMoviesInteractor()
  override fun trendingInteractor(): TrendingInteractor = TestTrendingInteractor()
  override fun dateFormatter(): DateFormatter =
    object : DateFormatter {
      override fun formatDate(date: String): String = ""
    }

  override fun configurationWorkerCreator(): WorkManager.WorkerCreator =
    object : WorkManager.WorkerCreator {
      override fun createWorkRequest(context: Context, params: WorkerParameters): Worker = null!!
    }
}
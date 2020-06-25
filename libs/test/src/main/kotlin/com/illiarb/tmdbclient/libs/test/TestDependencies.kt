package com.illiarb.tmdbclient.libs.test

import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.illiarb.tmdbclient.libs.test.analytics.TestAnalyticsService
import com.illiarb.tmdbclient.libs.test.interactor.TestFiltersInteractor
import com.illiarb.tmdbclient.libs.test.interactor.TestGenresInteractor
import com.illiarb.tmdbclient.libs.test.interactor.TestHomeInteractor
import com.illiarb.tmdbclient.libs.test.interactor.TestMoviesInteractor
import com.illiarb.tmdbclient.libs.test.interactor.TestTrendingInteractor
import com.illiarb.tmdbclient.libs.test.tools.TestConnectivityStatus
import com.illiarb.tmdbclient.libs.test.tools.TestDispatcherProvider
import com.illiarb.tmdbclient.libs.test.tools.TestFeatureFlagStore
import com.illiarb.tmdbclient.libs.test.tools.TestResourceResolver
import com.illiarb.tmdbclient.libs.tools.ConnectivityStatus
import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.libs.tools.FeatureFlagStore
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.libs.tools.di.ToolsProvider
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import com.illiarb.tmdbclient.services.analytics.di.AnalyticsProvider
import com.illiarb.tmdbclient.services.tmdb.di.TmdbProvider
import com.illiarb.tmdbclient.services.tmdb.api.interactor.FiltersInteractor
import com.illiarb.tmdbclient.services.tmdb.api.interactor.GenresInteractor
import com.illiarb.tmdbclient.services.tmdb.api.interactor.HomeInteractor
import com.illiarb.tmdbclient.services.tmdb.api.interactor.MoviesInteractor
import com.illiarb.tmdbclient.services.tmdb.api.interactor.TrendingInteractor

@Suppress("TooManyFunctions")
object TestDependencies : ToolsProvider, AnalyticsProvider, TmdbProvider {
  override fun connectivityStatus(): ConnectivityStatus = TestConnectivityStatus()
  override fun resourceResolver(): ResourceResolver = TestResourceResolver()
  override fun dispatcherProvider(): DispatcherProvider = TestDispatcherProvider()
  override fun featureFlagStore(): FeatureFlagStore = TestFeatureFlagStore()
  override fun analyticsService(): AnalyticsService = TestAnalyticsService()
  override fun homeInteractor(): HomeInteractor = TestHomeInteractor()
  override fun genresInteractor(): GenresInteractor = TestGenresInteractor()
  override fun moviesInteractor(): MoviesInteractor = TestMoviesInteractor()
  override fun trendingInteractor(): TrendingInteractor = TestTrendingInteractor()
  override fun networkFlipperPlugin(): NetworkFlipperPlugin = NetworkFlipperPlugin()
  override fun filtersInteractor(): FiltersInteractor = TestFiltersInteractor()
}
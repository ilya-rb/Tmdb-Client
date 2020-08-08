package com.illiarb.tmdbclient.libs.test

import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.illiarb.tmdbclient.libs.test.analytics.TestAnalyticsService
import com.illiarb.tmdbclient.libs.test.interactor.TestDiscoverInteractor
import com.illiarb.tmdbclient.libs.test.interactor.TestFiltersInteractor
import com.illiarb.tmdbclient.libs.test.interactor.TestGenresInteractor
import com.illiarb.tmdbclient.libs.test.interactor.TestHomeInteractor
import com.illiarb.tmdbclient.libs.test.interactor.TestMoviesInteractor
import com.illiarb.tmdbclient.libs.test.interactor.TestSearchInteractor
import com.illiarb.tmdbclient.libs.test.interactor.TestTrendingInteractor
import com.illiarb.tmdbclient.libs.test.tools.TestConnectivityStatus
import com.illiarb.tmdbclient.libs.test.tools.TestDispatcherProvider
import com.illiarb.tmdbclient.libs.test.tools.TestFeatureFlagStore
import com.illiarb.tmdbclient.libs.test.tools.TestResourceResolver
import com.illiarb.tmdbclient.libs.tools.ConnectivityStatus
import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.libs.tools.FeatureFlagStore
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import com.illiarb.tmdbclient.services.tmdb.interactor.DiscoverInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.FiltersInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.GenresInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.HomeInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.SearchInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.TrendingInteractor

@Suppress("TooManyFunctions")
object TestDependencies  {
  fun connectivityStatus(): ConnectivityStatus = TestConnectivityStatus()
  fun resourceResolver(): ResourceResolver = TestResourceResolver()
  fun dispatcherProvider(): DispatcherProvider = TestDispatcherProvider()
  fun featureFlagStore(): FeatureFlagStore = TestFeatureFlagStore()
  fun analyticsService(): AnalyticsService = TestAnalyticsService()
  fun homeInteractor(): HomeInteractor = TestHomeInteractor()
  fun genresInteractor(): GenresInteractor = TestGenresInteractor()
  fun moviesInteractor(): MoviesInteractor = TestMoviesInteractor()
  fun trendingInteractor(): TrendingInteractor = TestTrendingInteractor()
  fun networkFlipperPlugin(): NetworkFlipperPlugin = NetworkFlipperPlugin()
  fun filtersInteractor(): FiltersInteractor = TestFiltersInteractor()
  fun searchInteractor(): SearchInteractor = TestSearchInteractor()
  fun discoverInteractor(): DiscoverInteractor = TestDiscoverInteractor()
}
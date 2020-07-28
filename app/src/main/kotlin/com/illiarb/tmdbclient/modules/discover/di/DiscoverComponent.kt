package com.illiarb.tmdbclient.modules.discover.di

import com.illiarb.tmdbclient.libs.ui.di.ViewModelModule
import com.illiarb.tmdbclient.modules.discover.DiscoverFragment
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import com.illiarb.tmdbclient.services.tmdb.interactor.DiscoverInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.FiltersInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.SearchInteractor
import dagger.Component

@Component(
  dependencies = [DiscoverComponent.Dependencies::class],
  modules = [
    DiscoverModule::class,
    ViewModelModule::class
  ]
)
interface DiscoverComponent {

  interface Dependencies {
    fun router(): Router
    fun analyticsService(): AnalyticsService
    fun filtersInteractor(): FiltersInteractor
    fun searchInteractor(): SearchInteractor
    fun discoverInteractor(): DiscoverInteractor
  }

  @Component.Factory
  interface Factory {
    fun create(dependencies: Dependencies): DiscoverComponent
  }

  fun inject(fragment: DiscoverFragment)
}
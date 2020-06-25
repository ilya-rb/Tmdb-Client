package com.illiarb.tmdbclient.modules.home.di

import com.illiarb.tmdbclient.libs.ui.di.ViewModelModule
import com.illiarb.tmdbclient.modules.home.HomeFragment
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import com.illiarb.tmdbclient.services.tmdb.api.interactor.FiltersInteractor
import com.illiarb.tmdbclient.services.tmdb.api.interactor.HomeInteractor
import com.illiarb.tmdbclient.services.tmdb.api.interactor.TrendingInteractor
import dagger.Component

@Component(
  dependencies = [HomeComponent.Dependencies::class],
  modules = [
    HomeModule::class,
    ViewModelModule::class
  ]
)
interface HomeComponent {

  interface Dependencies {
    fun homeInteractor(): HomeInteractor
    fun trendingInteractor(): TrendingInteractor
    fun filtersInteractor(): FiltersInteractor
    fun router(): Router
    fun analyticsService(): AnalyticsService
  }

  @Component.Builder
  interface Builder {
    fun dependencies(dependencies: Dependencies): Builder
    fun build(): HomeComponent
  }

  fun inject(fragment: HomeFragment)
}
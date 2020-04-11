package com.illiarb.tmdbclient.ui.home.di

import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.ui.home.HomeFragment
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdclient.analytics.AnalyticsService
import com.tmdbclient.servicetmdb.interactor.HomeInteractor
import com.tmdbclient.servicetmdb.interactor.TrendingInteractor
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
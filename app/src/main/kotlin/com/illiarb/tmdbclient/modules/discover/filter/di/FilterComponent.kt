package com.illiarb.tmdbclient.modules.discover.filter.di

import com.illiarb.tmdbclient.libs.ui.di.ViewModelModule
import com.illiarb.tmdbclient.modules.discover.filter.FilterFragment
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.services.tmdb.api.interactor.FiltersInteractor
import com.illiarb.tmdbclient.services.tmdb.api.interactor.GenresInteractor
import dagger.Component

@Component(
  dependencies = [FilterComponent.Dependencies::class],
  modules = [
    ViewModelModule::class,
    FilterModule::class
  ]
)
interface FilterComponent {

  interface Dependencies {
    fun genresInteractor(): GenresInteractor
    fun filtersInteractor(): FiltersInteractor
    fun router(): Router
  }

  @Component.Builder
  interface Builder {
    fun dependencies(dependencies: Dependencies): Builder
    fun build(): FilterComponent
  }

  fun inject(fragment: FilterFragment)
}
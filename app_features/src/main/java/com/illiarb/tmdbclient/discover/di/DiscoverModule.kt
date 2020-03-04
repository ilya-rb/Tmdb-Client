package com.illiarb.tmdbclient.discover.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.discover.DiscoverViewModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.interactor.GenresInteractor
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class DiscoverModule(private val genreId: Int) {

  @Provides
  @IntoMap
  @ViewModelKey(DiscoverViewModel::class)
  fun provideDiscoverViewModel(
    genresInteractor: GenresInteractor,
    moviesInteractor: MoviesInteractor,
    router: Router,
    analyticsService: AnalyticsService
  ): ViewModel {
    return DiscoverViewModel(
      genreId,
      router,
      moviesInteractor,
      genresInteractor,
      analyticsService
    )
  }
}
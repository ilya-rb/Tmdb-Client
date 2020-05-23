package com.illiarb.tmdbclient.modules.details.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.libs.ui.di.ViewModelKey
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author ilya-rb on 08.01.19.
 */
@Module
interface MovieDetailsModule {

  @Binds
  @IntoMap
  @ViewModelKey(MovieDetailsViewModel::class)
  fun bindMovieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel
}
package com.illiarb.tmdbclient.feature.movies.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.feature.movies.details.MovieDetailsViewModel
import com.illiarb.tmdbclient.feature.movies.list.MoviesViewModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MoviesModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    fun bindMoviesViewModel(viewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    fun bindMovieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel
}
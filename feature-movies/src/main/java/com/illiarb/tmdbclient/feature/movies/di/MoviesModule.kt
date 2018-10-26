package com.illiarb.tmdbclient.feature.movies.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.feature.movies.details.MovieDetailsViewModel
import com.illiarb.tmdbclient.feature.movies.details.reviews.MovieDetailsReviewViewModel
import com.illiarb.tmdbclient.feature.movies.movieslist.MoviesViewModel
import com.illiarb.tmdbclient.feature.movies.navigation.ShowMovieDetailsActionImpl
import com.illiarb.tmdbclient.feature.movies.navigation.ShowMovieFiltersActionImpl
import com.illiarb.tmdbclient.feature.movies.navigation.ShowMoviesListActionImpl
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import com.illiarb.tmdblcient.core.navigation.ShowMovieDetailsAction
import com.illiarb.tmdblcient.core.navigation.ShowMovieFiltersAction
import com.illiarb.tmdblcient.core.navigation.ShowMoviesListAction
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

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsReviewViewModel::class)
    fun bindMovieReviewsViewModel(viewModel: MovieDetailsReviewViewModel): ViewModel

    @Binds
    fun bindStartScreenAction(showMoviesListActionImpl: ShowMoviesListActionImpl): ShowMoviesListAction

    @Binds
    fun bindShowMovieDetailsAction(impl: ShowMovieDetailsActionImpl): ShowMovieDetailsAction

    @Binds
    fun bindShowMovieFiltersAction(impl: ShowMovieFiltersActionImpl): ShowMovieFiltersAction
}
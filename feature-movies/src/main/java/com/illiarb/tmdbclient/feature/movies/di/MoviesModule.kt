package com.illiarb.tmdbclient.feature.movies.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.feature.movies.details.MovieDetailsViewModel
import com.illiarb.tmdbclient.feature.movies.details.reviews.MovieDetailsReviewViewModel
import com.illiarb.tmdbclient.feature.movies.movieslist.MoviesViewModel
import com.illiarb.tmdbclient.feature.movies.navigation.MoviesScreenAction
import com.illiarb.tmdbclient.feature.movies.navigation.ShowMovieDetailsActionImpl
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import com.illiarb.tmdblcient.core.navigation.ShowMovieDetailsAction
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
    fun bindStartScreenAction(moviesScreenAction: MoviesScreenAction): ShowMoviesListAction

    @Binds
    fun bindShowMovieDetailsAction(impl: ShowMovieDetailsActionImpl): ShowMovieDetailsAction
}
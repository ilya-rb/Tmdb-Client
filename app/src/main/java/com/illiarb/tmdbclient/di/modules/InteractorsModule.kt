package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.feature.home.details.interactor.MovieDetailsInteractorImpl
import com.illiarb.tmdbclient.feature.home.list.interactor.MoviesInteractorImpl
import com.illiarb.tmdbclient.feature.search.interactor.SearchInteractorImpl
import com.illiarb.tmdblcient.core.modules.movie.MovieDetailsInteractor
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import com.illiarb.tmdblcient.core.modules.search.SearchInteractor
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 03.11.18.
 */
@Module
interface InteractorsModule {

    @Binds
    fun bindMoviesInteractor(impl: MoviesInteractorImpl): MoviesInteractor

    @Binds
    fun bindMovieDetailsInteractor(impl: MovieDetailsInteractorImpl): MovieDetailsInteractor

    @Binds
    fun bindSearchInteractor(impl: SearchInteractorImpl): SearchInteractor
}
package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.movies.domain.MoviesInteractorImpl
import com.illiarb.tmdblcient.core.domain.MoviesInteractor
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 18.02.19.
 */
@Module
interface InteractorsModule {

    @Binds
    fun bindMoviesInteractor(impl: MoviesInteractorImpl): MoviesInteractor
}
package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.home.domain.MoviesInteractorImpl
import com.illiarb.tmdbclient.feature.search.domain.SearchInteractorImpl
import com.illiarb.tmdblcient.core.domain.MoviesInteractor
import com.illiarb.tmdblcient.core.domain.SearchInteractor
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 18.02.19.
 */
@Module
interface InteractorsModule {

    @Binds
    fun bindMoviesInteractor(impl: MoviesInteractorImpl): MoviesInteractor

    @Binds
    fun bindSearchInteractor(impl: SearchInteractorImpl): SearchInteractor
}
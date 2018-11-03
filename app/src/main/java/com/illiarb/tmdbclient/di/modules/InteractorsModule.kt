package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.coreimpl.explore.LocationInteractorImpl
import com.illiarb.tmdbclient.coreimpl.movie.MoviesInteractorImpl
import com.illiarb.tmdblcient.core.modules.location.LocationInteractor
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
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
    fun bindLocationInteractor(impl: LocationInteractorImpl): LocationInteractor
}
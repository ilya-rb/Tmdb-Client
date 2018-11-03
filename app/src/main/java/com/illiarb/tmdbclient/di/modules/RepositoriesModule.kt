package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.storage.repositories.LocationRepositoryImpl
import com.illiarb.tmdbclient.storage.repositories.MoviesRepositoryImpl
import com.illiarb.tmdblcient.core.modules.location.LocationRepository
import com.illiarb.tmdblcient.core.modules.movie.MoviesRepository
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 03.11.18.
 */
@Module
interface RepositoriesModule {

    @Binds
    fun bindMoviesRepository(impl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository
}
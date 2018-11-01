package com.illiarb.tmdbclient.storage.di

import com.illiarb.tmdbclient.storage.repositories.LocationRepositoryImpl
import com.illiarb.tmdbclient.storage.repositories.MoviesRepositoryImpl
import com.illiarb.tmdblcient.core.modules.location.LocationRepository
import com.illiarb.tmdblcient.core.modules.movie.MoviesRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoriesModule {

    @Binds
    fun bindMoviesRepository(impl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository
}
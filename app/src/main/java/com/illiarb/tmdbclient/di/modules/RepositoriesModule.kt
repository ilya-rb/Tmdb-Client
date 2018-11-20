package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.storage.repositories.AccountRepositoryImpl
import com.illiarb.tmdbclient.storage.repositories.LocationRepositoryImpl
import com.illiarb.tmdbclient.storage.repositories.MoviesRepositoryImpl
import com.illiarb.tmdblcient.core.storage.AccountRepository
import com.illiarb.tmdblcient.core.storage.LocationRepository
import com.illiarb.tmdblcient.core.storage.MoviesRepository
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

    @Binds
    fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository
}
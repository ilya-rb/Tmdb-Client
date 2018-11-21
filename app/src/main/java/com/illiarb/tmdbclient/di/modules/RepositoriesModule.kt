package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.storage.auth.TmdbAuthenticator
import com.illiarb.tmdbclient.storage.repositories.AccountRepositoryImpl
import com.illiarb.tmdbclient.storage.repositories.LocationRepositoryImpl
import com.illiarb.tmdbclient.storage.repositories.MoviesRepositoryImpl
import com.illiarb.tmdblcient.core.modules.account.AccountRepository
import com.illiarb.tmdblcient.core.modules.auth.Authenticator
import com.illiarb.tmdblcient.core.modules.explore.LocationRepository
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

    @Binds
    fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    fun bindAuthenticator(impl: TmdbAuthenticator): Authenticator
}
package com.illiarb.tmdbclient.storage.di.modules

import com.illiarb.tmdbclient.storage.auth.ErrorMessageBagImpl
import com.illiarb.tmdbclient.storage.auth.TmdbAuthenticator
import com.illiarb.tmdbclient.storage.repositories.AccountRepositoryImpl
import com.illiarb.tmdbclient.storage.repositories.MoviesRepositoryImpl
import com.illiarb.tmdblcient.core.auth.Authenticator
import com.illiarb.tmdblcient.core.exception.ErrorMessageBag
import com.illiarb.tmdblcient.core.repository.AccountRepository
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 24.12.18.
 */
@Module
interface RepositoriesModule {

    @Binds
    fun bindMoviesRepository(impl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    fun bindAuthenticator(impl: TmdbAuthenticator): Authenticator

    @Binds
    fun bindErrorMessageBar(impl: ErrorMessageBagImpl): ErrorMessageBag
}
package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.auth.Authenticator
import com.illiarb.tmdblcient.core.repository.AccountRepository
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import com.illiarb.tmdblcient.core.system.ConnectivityStatus
import com.illiarb.tmdblcient.core.system.ErrorMessageBag
import com.illiarb.tmdblcient.core.system.ResourceResolver
import com.illiarb.tmdblcient.core.system.WorkManager

/**
 * @author ilya-rb on 24.12.18.
 */
interface StorageProvider {

    fun provideMoviesRepository(): MoviesRepository

    fun provideAccountRepository(): AccountRepository

    fun provideResourceResolver(): ResourceResolver

    fun provideAuthenticator(): Authenticator

    fun provideErrorMessageBar(): ErrorMessageBag

    fun provideWorkManager(): WorkManager

    fun provideConnectivityStatus(): ConnectivityStatus
}
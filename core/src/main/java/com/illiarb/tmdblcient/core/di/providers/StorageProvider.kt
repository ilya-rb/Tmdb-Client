package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.storage.*

/**
 * @author ilya-rb on 24.12.18.
 */
interface StorageProvider {

    fun provideMoviesRepository(): MoviesRepository

    fun provideAccountRepository(): AccountRepository

    fun provideResourceResolver(): ResourceResolver

    fun provideAuthenticator(): Authenticator

    fun provideErrorMessageBag(): ErrorMessageBag

    fun provideWorkManager(): WorkManager

    fun provideErrorHandler(): ErrorHandler
}
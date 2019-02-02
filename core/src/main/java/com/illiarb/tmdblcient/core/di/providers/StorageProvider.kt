package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.storage.AccountRepository
import com.illiarb.tmdblcient.core.storage.MoviesRepository
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import com.illiarb.tmdblcient.core.storage.Authenticator
import com.illiarb.tmdblcient.core.storage.ErrorMessageBag
import com.illiarb.tmdblcient.core.storage.WorkManager
import com.illiarb.tmdblcient.core.storage.ErrorHandler

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
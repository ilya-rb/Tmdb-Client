package com.illiarb.tmdbclient.storage.di.modules

import com.illiarb.tmdbclient.storage.error.DefaultErrorHandler
import com.illiarb.tmdbclient.storage.error.ErrorMessageBagImpl
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.illiarb.tmdblcient.core.storage.ErrorMessageBag
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 17.01.19.
 */
@Module
interface ErrorHandlingModule {

    @Binds
    fun bindErrorHadndler(impl: DefaultErrorHandler): ErrorHandler

    @Binds
    fun bindErrorMessageBar(impl: ErrorMessageBagImpl): ErrorMessageBag
}
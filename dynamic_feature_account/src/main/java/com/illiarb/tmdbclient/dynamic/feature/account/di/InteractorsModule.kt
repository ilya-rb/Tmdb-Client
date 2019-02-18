package com.illiarb.tmdbclient.dynamic.feature.account.di

import com.illiarb.tmdbclient.dynamic.feature.account.auth.domain.AuthInteractorImpl
import com.illiarb.tmdbclient.dynamic.feature.account.profile.domain.AccountInteractorImpl
import com.illiarb.tmdblcient.core.domain.AccountInteractor
import com.illiarb.tmdblcient.core.domain.AuthInteractor
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 18.02.19.
 */
@Module
interface InteractorsModule {

    @Binds
    fun bindAccountInteractor(impl: AccountInteractorImpl): AccountInteractor

    @Binds
    fun bindAuthInteractor(impl: AuthInteractorImpl): AuthInteractor
}
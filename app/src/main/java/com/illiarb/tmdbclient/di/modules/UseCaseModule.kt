package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdblcient.core.domain.auth.Authenticate
import com.illiarb.tmdblcient.core.domain.auth.AuthenticateImpl
import com.illiarb.tmdblcient.core.domain.auth.ValidateCredentials
import com.illiarb.tmdblcient.core.domain.auth.ValidateCredentialsImpl
import com.illiarb.tmdblcient.core.domain.profile.GetProfile
import com.illiarb.tmdblcient.core.domain.profile.GetProfileImpl
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 08.01.19.
 */
@Module
interface UseCaseModule {

    @Binds
    fun authenticate(impl: AuthenticateImpl): Authenticate

    @Binds
    fun validateCredentials(impl: ValidateCredentialsImpl): ValidateCredentials

    @Binds
    fun getProfile(impl: GetProfileImpl): GetProfile
}
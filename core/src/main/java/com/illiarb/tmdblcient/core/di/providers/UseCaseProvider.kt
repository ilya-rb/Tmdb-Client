package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.domain.auth.Authenticate
import com.illiarb.tmdblcient.core.domain.auth.ValidateCredentials
import com.illiarb.tmdblcient.core.domain.profile.GetProfile

/**
 * @author ilya-rb on 08.01.19.
 */
interface UseCaseProvider {

    fun authenticate(): Authenticate

    fun validateCredentials(): ValidateCredentials

    fun getProfile(): GetProfile
}
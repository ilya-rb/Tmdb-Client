package com.illiarb.tmdbclient.dynamic.feature.account.auth.domain

import com.illiarb.tmdblcient.core.auth.Authenticator
import com.illiarb.tmdblcient.core.domain.NonBlockingUseCase
import com.illiarb.tmdblcient.core.entity.UserCredentials
import com.illiarb.tmdblcient.core.system.NonBlocking
import javax.inject.Inject

/**
 * @author ilya-rb on 10.01.19.
 */
class Authenticate @Inject constructor(
    private val validateCredentials: ValidateCredentials,
    private val authenticator: Authenticator
) : NonBlockingUseCase<Boolean, UserCredentials> {

    @NonBlocking
    override suspend fun executeAsync(payload: UserCredentials): Boolean {
        val isCredentialsValid = validateCredentials.executeBlocking(payload)
        if (isCredentialsValid) {
            return authenticator.authorize(payload)
        }
        return false
    }
}
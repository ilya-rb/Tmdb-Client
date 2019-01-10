package com.illiarb.tmdbclient.dynamic.feature.account.auth.domain

import com.illiarb.tmdblcient.core.auth.Authenticator
import com.illiarb.tmdblcient.core.domain.UseCase
import com.illiarb.tmdblcient.core.entity.UserCredentials
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import com.illiarb.tmdblcient.core.system.NonBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author ilya-rb on 10.01.19.
 */
class Authenticate @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val validateCredentials: ValidateCredentials,
    private val authenticator: Authenticator
) : UseCase<Boolean, UserCredentials> {

    @NonBlocking
    override suspend fun execute(payload: UserCredentials): Boolean = withContext(dispatcherProvider.ioDispatcher) {
        val isCredentialsValid = validateCredentials.execute(payload)
        if (isCredentialsValid) {
            authenticator.authorize(payload)
        } else {
            false
        }
    }
}
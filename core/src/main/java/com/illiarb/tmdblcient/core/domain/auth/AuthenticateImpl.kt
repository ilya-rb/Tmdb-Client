package com.illiarb.tmdblcient.core.domain.auth

import com.illiarb.tmdblcient.core.system.DispatcherProvider
import com.illiarb.tmdblcient.core.system.NonBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class AuthenticateImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val validateCredentials: ValidateCredentials,
    private val authenticator: Authenticator
) : Authenticate {

    @NonBlocking
    override suspend fun invoke(username: String, password: String): Boolean = withContext(dispatcherProvider.ioDispatcher) {
        val isCredentialsValid = validateCredentials(username, password)
        if (isCredentialsValid) {
            authenticator.authorize(username, password)
        } else {
            false
        }
    }
}
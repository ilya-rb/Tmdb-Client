package com.illiarb.tmdbclient.storage.auth

import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbclient.storage.network.api.service.AuthService
import com.illiarb.tmdbclient.storage.network.request.CreateSessionRequest
import com.illiarb.tmdbclient.storage.network.request.ValidateTokenRequest
import com.illiarb.tmdblcient.core.storage.Authenticator
import com.illiarb.tmdblcient.core.domain.entity.UserCredentials
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author ilya-rb on 21.11.18.
 */
class TmdbAuthenticator @Inject constructor(
    private val persistableStorage: PersistableStorage,
    private val authService: AuthService,
    private val dispatcherProvider: DispatcherProvider
) : Authenticator {

    override suspend fun authorize(credentials: UserCredentials): Unit =
        withContext(dispatcherProvider.io) {
            val authToken = authService.requestAuthToken().await()

            val request = ValidateTokenRequest(credentials.username, credentials.password, authToken.requestToken)
            val validatedToken = authService.validateTokenWithCredentials(request).await()

            val session = authService.createNewSession(CreateSessionRequest(validatedToken.requestToken)).await()

            persistableStorage.storeSessionId(session.sessionId)

            Unit
        }

    override suspend fun isAuthenticated(): Boolean = withContext(dispatcherProvider.io) {
        persistableStorage.isAuthorized()
    }
}
package com.illiarb.tmdbclient.storage.auth

import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbclient.storage.network.api.service.AuthService
import com.illiarb.tmdbclient.storage.network.request.CreateSessionRequest
import com.illiarb.tmdbclient.storage.network.request.ValidateTokenRequest
import com.illiarb.tmdblcient.core.modules.auth.Authenticator
import io.reactivex.Completable
import javax.inject.Inject

/**
 * @author ilya-rb on 21.11.18.
 */
class TmdbAuthenticator @Inject constructor(
    private val persistableStorage: PersistableStorage,
    private val authService: AuthService
) : Authenticator {

    override fun authorize(username: String, password: String): Completable =
        authService.requestAuthToken()
            .flatMap { authService.validateTokenWithCredentials(ValidateTokenRequest(username, password, it.requestToken)) }
            .flatMap { authService.createNewSession(CreateSessionRequest(it.requestToken)) }
            .doOnSuccess { persistableStorage.storeSessionId(it.sessionId) }
            .ignoreElement()

    override fun isAuthenticated(): Boolean = persistableStorage.isAuthorized()
}
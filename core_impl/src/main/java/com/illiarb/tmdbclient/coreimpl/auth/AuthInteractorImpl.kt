package com.illiarb.tmdbclient.coreimpl.auth

import com.illiarb.tmdblcient.core.modules.auth.AuthInteractor
import com.illiarb.tmdblcient.core.modules.auth.Authenticator
import com.illiarb.tmdblcient.core.navigation.AccountScreenData
import com.illiarb.tmdblcient.core.navigation.Router
import io.reactivex.Completable
import javax.inject.Inject

/**
 * @author ilya-rb on 21.11.18.
 */
class AuthInteractorImpl @Inject constructor(
    private val authenticator: Authenticator,
    private val router: Router
) : AuthInteractor {

    override fun authenticate(username: String, password: String): Completable =
        authenticator.authorize(username, password).andThen { router.navigateTo(AccountScreenData) }
}
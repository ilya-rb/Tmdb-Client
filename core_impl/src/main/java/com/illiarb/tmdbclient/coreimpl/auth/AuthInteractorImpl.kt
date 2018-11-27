package com.illiarb.tmdbclient.coreimpl.auth

import com.illiarb.tmdblcient.core.exception.ErrorCodes
import com.illiarb.tmdblcient.core.exception.ValidationException
import com.illiarb.tmdblcient.core.ext.onCompleteSafe
import com.illiarb.tmdblcient.core.ext.onErrorSafe
import com.illiarb.tmdblcient.core.modules.auth.AuthInteractor
import com.illiarb.tmdblcient.core.modules.auth.Authenticator
import com.illiarb.tmdblcient.core.navigation.AccountScreenData
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.system.ErrorMessageBag
import io.reactivex.Completable
import javax.inject.Inject

/**
 * @author ilya-rb on 21.11.18.
 */
class AuthInteractorImpl @Inject constructor(
    private val authenticator: Authenticator,
    private val router: Router,
    private val validator: Validator,
    private val errorMessageBag: ErrorMessageBag
) : AuthInteractor {

    override fun authenticate(username: String, password: String): Completable =
        validateCredentials(username, password)
            .andThen { authenticator.authorize(username, password) }
            .andThen { router.navigateTo(AccountScreenData) }

    private fun validateCredentials(username: String, password: String): Completable =
        Completable.create { emitter ->
            val errors = mutableListOf<Pair<Int, String>>()

            if (validator.isUsernameEmpty(username)) {
                errors.add(Pair(ErrorCodes.ERROR_USERNAME_EMPTY, errorMessageBag.getUsernameEmptyMessage()))
            }

            if (validator.isPasswordEmpty(password)) {
                errors.add(Pair(ErrorCodes.ERROR_PASSWORD_EMPTY, errorMessageBag.getPasswordEmptyMessage()))
            }

            if (errors.isNotEmpty()) {
                emitter.onErrorSafe(ValidationException(errors))
            } else {
                emitter.onCompleteSafe()
            }
        }
}
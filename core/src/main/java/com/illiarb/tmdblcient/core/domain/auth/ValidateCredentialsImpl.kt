package com.illiarb.tmdblcient.core.domain.auth

import com.illiarb.tmdblcient.core.exception.ErrorCodes
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import com.illiarb.tmdblcient.core.system.ErrorMessageBag
import com.illiarb.tmdblcient.core.system.NonBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class ValidateCredentialsImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val validator: Validator,
    private val errorMessageBag: ErrorMessageBag
) : ValidateCredentials {

    @NonBlocking
    override suspend fun invoke(username: String, password: String): Boolean = withContext(dispatcherProvider.ioDispatcher) {
        val errors = mutableListOf<Pair<Int, String>>()

        if (validator.isUsernameEmpty(username)) {
            errors.add(Pair(ErrorCodes.ERROR_USERNAME_EMPTY, errorMessageBag.getUsernameEmptyMessage()))
        }

        if (validator.isPasswordEmpty(password)) {
            errors.add(Pair(ErrorCodes.ERROR_PASSWORD_EMPTY, errorMessageBag.getPasswordEmptyMessage()))
        } else if (!validator.isPasswordLengthCorrect(password)) {
            errors.add(Pair(ErrorCodes.ERROR_PASSWORD_LENGTH, errorMessageBag.getIncorrectPasswordLengthMessage()))
        }

        // If errors is empty - credentials are valid
        errors.isEmpty()
    }
}
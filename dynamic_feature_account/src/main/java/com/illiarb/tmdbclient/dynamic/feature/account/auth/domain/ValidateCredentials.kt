package com.illiarb.tmdbclient.dynamic.feature.account.auth.domain

import com.illiarb.tmdblcient.core.domain.UseCase
import com.illiarb.tmdblcient.core.entity.UserCredentials
import com.illiarb.tmdblcient.core.exception.ErrorCodes
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import com.illiarb.tmdblcient.core.system.ErrorMessageBag
import com.illiarb.tmdblcient.core.system.NonBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author ilya-rb on 10.01.19.
 */
class ValidateCredentials @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val validator: Validator,
    private val errorMessageBag: ErrorMessageBag
) : UseCase<Boolean, UserCredentials> {

    @NonBlocking
    override suspend fun execute(payload: UserCredentials): Boolean = withContext(dispatcherProvider.ioDispatcher) {
        val errors = mutableListOf<Pair<Int, String>>()

        if (validator.isUsernameEmpty(payload.username)) {
            errors.add(Pair(ErrorCodes.ERROR_USERNAME_EMPTY, errorMessageBag.getUsernameEmptyMessage()))
        }

        if (validator.isPasswordEmpty(payload.password)) {
            errors.add(Pair(ErrorCodes.ERROR_PASSWORD_EMPTY, errorMessageBag.getPasswordEmptyMessage()))
        } else if (!validator.isPasswordLengthCorrect(payload.password)) {
            errors.add(Pair(ErrorCodes.ERROR_PASSWORD_LENGTH, errorMessageBag.getIncorrectPasswordLengthMessage()))
        }

        // If errors is empty - credentials are valid
        errors.isEmpty()
    }
}
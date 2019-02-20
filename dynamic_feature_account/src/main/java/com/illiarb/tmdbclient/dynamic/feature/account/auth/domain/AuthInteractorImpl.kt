package com.illiarb.tmdbclient.dynamic.feature.account.auth.domain

import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.AuthInteractor
import com.illiarb.tmdblcient.core.domain.entity.UserCredentials
import com.illiarb.tmdblcient.core.exception.ErrorCodes
import com.illiarb.tmdblcient.core.exception.ValidationException
import com.illiarb.tmdblcient.core.storage.Authenticator
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.illiarb.tmdblcient.core.storage.ErrorMessageBag
import javax.inject.Inject

/**
 * @author ilya-rb on 18.02.19.
 */
class AuthInteractorImpl @Inject constructor(
    private val authenticator: Authenticator,
    private val validator: Validator,
    private val errorMessageBag: ErrorMessageBag,
    private val errorHandler: ErrorHandler,
    private val analyticsService: AnalyticsService
) : AuthInteractor {

    override suspend fun authenticate(credentials: UserCredentials): Result<Unit> {
        val validationResult = validateCredentials(credentials)
        if (validationResult is Result.Error) {
            return validationResult
        }

        return Result
            .create(errorHandler) { authenticator.authorize(credentials) }
            .doOnSuccess {
                analyticsService.trackEvent(analyticsService.factory.createLoggedInEvent())
            }
    }

    override suspend fun isAuthenticated(): Boolean = authenticator.isAuthenticated()

    fun validateCredentials(credentials: UserCredentials): Result<Unit> {
        val errors = mutableListOf<Pair<Int, String>>()

        if (validator.isUsernameEmpty(credentials.username)) {
            errors.add(
                Pair(
                    ErrorCodes.ERROR_USERNAME_EMPTY,
                    errorMessageBag.getUsernameEmptyMessage()
                )
            )
        }

        if (validator.isPasswordEmpty(credentials.password)) {
            errors.add(
                Pair(
                    ErrorCodes.ERROR_PASSWORD_EMPTY,
                    errorMessageBag.getPasswordEmptyMessage()
                )
            )
        } else if (!validator.isPasswordLengthCorrect(credentials.password)) {
            errors.add(
                Pair(
                    ErrorCodes.ERROR_PASSWORD_LENGTH,
                    errorMessageBag.getIncorrectPasswordLengthMessage()
                )
            )
        }

        return if (errors.isEmpty()) {
            Result.Success(Unit)
        } else {
            Result.Error(ValidationException(errors))
        }
    }
}
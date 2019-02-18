package com.illiarb.tmdbclient.dynamic.feature.account.auth

import com.illiarb.tmdbclient.dynamic.feature.account.auth.domain.AuthInteractorImpl
import com.illiarb.tmdbclient.dynamic.feature.account.auth.domain.Validator
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.entity.UserCredentials
import com.illiarb.tmdblcient.core.exception.ErrorCodes
import com.illiarb.tmdblcient.core.exception.ValidationException
import com.illiarb.tmdblcient.core.storage.Authenticator
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.illiarb.tmdblcient.core.storage.ErrorMessageBag
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

/**
 * @author ilya-rb on 18.02.19.
 */
class AuthInteractorTest {

    private val validator = Validator()
    private val errorMessageBag = mock<ErrorMessageBag>()
    private val authenticator = mock<Authenticator>()
    private val errorHandler = mock<ErrorHandler>()

    private val authInteractor =
        AuthInteractorImpl(authenticator, validator, errorMessageBag, errorHandler)

    @Test
    fun `on invalid credentials validation error is received`() {
        val credentials = FakeEntityFactory.createPasswordInvalidCredentials()

        runBlocking {
            val result = authInteractor.authenticate(credentials)

            verifyZeroInteractions(authenticator)

            Assert.assertTrue(result is Result.Error)

            val errorResult = result as Result.Error
            Assert.assertTrue(errorResult.error is ValidationException)
        }
    }

    @Test
    fun `on valid credentials authenticate successful`() {
        val credentials = FakeEntityFactory.createValidCredentials()

        runBlocking {
            Mockito
                .`when`(authenticator.authorize(credentials))
                .thenReturn(Unit)

            val result = authInteractor.authenticate(credentials)

            verify(authenticator).authorize(credentials)

            Assert.assertTrue(result is Result.Success)
        }
    }

    @Test
    fun `on invalid credentials error codes is returned`() {

        // Validate empty credentials
        val emptyCredentials = UserCredentials("", "")
        val errors =
            getValidationErrorsFromResult(authInteractor.validateCredentials(emptyCredentials))

        Assert.assertTrue(errors.isNotEmpty())
        Assert.assertEquals(2, errors.size)

        Assert.assertTrue(errors.find { it.first == ErrorCodes.ERROR_USERNAME_EMPTY } != null)
        Assert.assertTrue(errors.find { it.first == ErrorCodes.ERROR_PASSWORD_EMPTY } != null)

        // Valid for password length
        val passwordCredentials = FakeEntityFactory.createPasswordInvalidCredentials()
        val passwordResult =
            getValidationErrorsFromResult(authInteractor.validateCredentials(passwordCredentials))

        Assert.assertTrue(passwordResult.find { it.first == ErrorCodes.ERROR_PASSWORD_LENGTH } != null)
    }

    @Test
    fun `on valid credentials success is returned`() {
        val validCredentials = FakeEntityFactory.createValidCredentials()
        val result = authInteractor.validateCredentials(validCredentials)
        Assert.assertTrue(result is Result.Success)
    }

    private fun getValidationErrorsFromResult(result: Result<*>): List<Pair<Int, String>> {
        val error = result as Result.Error
        val throwable = error.error as ValidationException
        return throwable.errors
    }
}
package com.illiarb.tmdbclient.dynamic.feature.account.auth

import com.illiarb.tmdbclient.dynamic.feature.account.auth.domain.Authenticate
import com.illiarb.tmdbclient.dynamic.feature.account.auth.domain.ValidateCredentials
import com.illiarb.tmdbclient.dynamic.feature.account.auth.domain.Validator
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.storage.Authenticator
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.illiarb.tmdblcient.core.storage.ErrorMessageBag
import com.illiarb.tmdblcient.core.exception.ValidationException
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito

/**
 * @author ilya-rb on 26.01.19.
 */
class AuthenticateTest {

    private val validator = Validator()
    private val errorMessageBag = mock<ErrorMessageBag>()

    private val validateCredentials = ValidateCredentials(validator, errorMessageBag)

    private val errorHandler = mock<ErrorHandler>()
    private val authenticator = mock<Authenticator>()

    private val authenticateUseCase = Authenticate(validateCredentials, authenticator, errorHandler)

    @Test
    fun `on invalid credentials validation error is received`() {
        val credentials = FakeEntityFactory.createPasswordInvalidCredentials()

        runBlocking {
            val result = authenticateUseCase.executeAsync(credentials)

            verifyZeroInteractions(authenticator)

            assertTrue(result is Result.Error)

            val errorResult = result as Result.Error
            assertTrue(errorResult.error is ValidationException)
        }
    }

    @Test
    fun `on valid credentials authenticate successful`() {
        val credentials = FakeEntityFactory.createValidCredentials()

        runBlocking {
            Mockito
                .`when`(authenticator.authorize(credentials))
                .thenReturn(Unit)

            val result = authenticateUseCase.executeAsync(credentials)

            verify(authenticator).authorize(credentials)

            assertTrue(result is Result.Success)
        }
    }

}
package com.illiarb.tmdbclient.dynamic.feature.account.profile

import com.illiarb.tmdbclient.dynamic.feature.account.profile.domain.SignOutUseCase
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.exception.ErrorHandler
import com.illiarb.tmdblcient.core.repository.AccountRepository
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
class SignOutUseCaseTest {

    private val repository = mock<AccountRepository>()
    private val errorHandler = mock<ErrorHandler>()

    private val signOutUserCase = SignOutUseCase(repository, errorHandler)

    @Test
    fun `on sign out success true is returned`() {
        runBlocking {
            Mockito
                .`when`(repository.clearAccountData())
                .thenReturn(true)

            val result = signOutUserCase.executeAsync(Unit)

            verify(repository).clearAccountData()
            verifyZeroInteractions(errorHandler)

            assertTrue(result is Result.Success)

            val successResult = result as Result.Success
            assertTrue(successResult.result)
        }
    }

    @Test
    fun `on sign out failure error is returned`() {
        runBlocking {
            val error = RuntimeException("Error Clearing data")

            Mockito
                .`when`(errorHandler.createExceptionFromThrowable(error))
                .thenReturn(error)

            Mockito
                .`when`(repository.clearAccountData())
                .thenThrow(error)

            val result = signOutUserCase.executeAsync(Unit)

            verify(repository).clearAccountData()
            verify(errorHandler).createExceptionFromThrowable(error)

            assertTrue(result is Result.Error)
        }
    }
}
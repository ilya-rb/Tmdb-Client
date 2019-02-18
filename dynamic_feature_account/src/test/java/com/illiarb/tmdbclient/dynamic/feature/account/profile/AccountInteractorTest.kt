package com.illiarb.tmdbclient.dynamic.feature.account.profile

import com.illiarb.tmdbclient.dynamic.feature.account.profile.domain.AccountInteractorImpl
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.storage.AccountRepository
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions

/**
 * @author ilya-rb on 18.02.19.
 */
class AccountInteractorTest {

    private val repository = mock<AccountRepository>()
    private val errorHandler = mock<ErrorHandler>()

    private val accountInteractor = AccountInteractorImpl(repository, errorHandler)

    @Test
    fun `on success profile is returned`() {
        runBlocking {
            val favoriteMovies = FakeEntityFactory.createFakeMovieList(size = 5)
            val ratedMovies = FakeEntityFactory.createFakeMovieList(size = 4)
            val account = FakeEntityFactory.createFakeAccount()

            Mockito
                .`when`(repository.getFavoriteMovies(account.id))
                .thenReturn(favoriteMovies)

            Mockito
                .`when`(repository.getRatedMovies(account.id))
                .thenReturn(ratedMovies)

            Mockito
                .`when`(repository.getCurrentAccount())
                .thenReturn(account)

            val result = accountInteractor.getAccount()

            verify(repository).getFavoriteMovies(account.id)
            verify(repository).getRatedMovies(account.id)
            verify(repository).getCurrentAccount()

            assertTrue(result is Result.Success)

            val accountResult = result as Result.Success
            assertTrue(accountResult.result.favoriteMovies.isNotEmpty())

            val rating = ratedMovies
                .map { it.rating }
                .average()
                .toInt()

            assertEquals(rating * 10, accountResult.result.averageRating)
        }
    }

    @Test
    fun `on sign out success true is returned`() {
        runBlocking {
            Mockito
                .`when`(repository.clearAccountData())
                .thenReturn(true)

            val result = accountInteractor.exitFromAccount()

            verify(repository).clearAccountData()
            verifyZeroInteractions(errorHandler)

            assertTrue(result is Result.Success)
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

            val result = accountInteractor.exitFromAccount()

            verify(repository).clearAccountData()
            verify(errorHandler).createExceptionFromThrowable(error)

            assertTrue(result is Result.Error)
        }
    }
}
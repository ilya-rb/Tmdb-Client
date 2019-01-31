package com.illiarb.tmdbclient.dynamic.feature.account.profile

import com.illiarb.tmdbclient.dynamic.feature.account.profile.domain.GetProfileUseCase
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.illiarb.tmdblcient.core.storage.AccountRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito

/**
 * @author ilya-rb on 26.01.19.
 */
class GetProfileUseCaseTest {

    private val repository = mock<AccountRepository>()
    private val errorHandler = mock<ErrorHandler>()

    private val getProfileUseCase = GetProfileUseCase(repository, errorHandler)

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

            val result = getProfileUseCase.executeAsync(Unit)

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
}
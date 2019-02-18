package com.illiarb.tmdbclient.feature.search

import com.illiarb.tmdbclient.feature.search.domain.SearchInteractorImpl
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.illiarb.tmdblcient.core.storage.MoviesRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import java.util.*

/**
 * @author ilya-rb on 18.02.19.
 */
class SearchInteractorTest {

    private val repository = mock<MoviesRepository>()
    private val errorHandler = mock<ErrorHandler>()

    private val searchInteractor = SearchInteractorImpl(repository, errorHandler)

    @Test
    fun `on search query results not empty and result success`() {
        runBlocking {
            val query = "test"

            Mockito
                .`when`(repository.searchMovies(query))
                .thenReturn(FakeEntityFactory.createFakeMovieList(size = 5))

            val result = searchInteractor.searchMovies(query)

            verify(repository).searchMovies(query)

            Assert.assertTrue(result is Result.Success)

            val successResult = result as Result.Success
            Assert.assertTrue(successResult.result.isNotEmpty())
        }
    }

    @Test
    fun `on bad query empty results is returned`() {
        runBlocking {
            val query = "test"

            Mockito
                .`when`(repository.searchMovies(query))
                .thenReturn(Collections.emptyList())

            val result = searchInteractor.searchMovies(query)

            verify(repository).searchMovies(query)

            Assert.assertTrue(result is Result.Success)

            val successResult = result as Result.Success
            Assert.assertTrue(successResult.result.isEmpty())
        }
    }
}
package com.illiarb.tmdbclient.feature.search

import com.illiarb.tmdbclient.feature.search.domain.SearchMovies
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.exception.ErrorHandler
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import java.util.*

/**
 * @author ilya-rb on 28.01.19.
 */
class SearchMoviesTest {

    private val repository = mock<MoviesRepository>()
    private val errorHandler = mock<ErrorHandler>()

    private val searchMovies = SearchMovies(repository, errorHandler)

    @Test
    fun `on search query results not empty and result success`() {
        runBlocking {
            val query = "test"

            Mockito
                .`when`(repository.searchMovies(query))
                .thenReturn(FakeEntityFactory.createFakeMovieList(size = 5))

            val result = searchMovies.executeAsync(query)

            verify(repository).searchMovies(query)

            assertTrue(result is Result.Success)

            val successResult = result as Result.Success
            assertTrue(successResult.result.isNotEmpty())
        }
    }

    @Test
    fun `on bad query empty results is returned`() {
        runBlocking {
            val query = "test"

            Mockito
                .`when`(repository.searchMovies(query))
                .thenReturn(Collections.emptyList())

            val result = searchMovies.executeAsync(query)

            verify(repository).searchMovies(query)

            assertTrue(result is Result.Success)

            val successResult = result as Result.Success
            assertTrue(successResult.result.isEmpty())
        }
    }

}
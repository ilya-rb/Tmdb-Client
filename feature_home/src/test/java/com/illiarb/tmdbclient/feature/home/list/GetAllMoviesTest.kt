package com.illiarb.tmdbclient.feature.home.list

import com.illiarb.tmdbclient.feature.home.list.domain.GetAllMovies
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.entity.MovieFilter
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.illiarb.tmdblcient.core.storage.MoviesRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito

/**
 * @author ilya-rb on 28.01.19.
 */
class GetAllMoviesTest {

    private val repository = mock<MoviesRepository>()
    private val errorHandler = mock<ErrorHandler>()

    private val getAllMovies = GetAllMovies(repository, errorHandler)

    @Test
    fun `on movies fetched now playing section is first`() {
        runBlocking {
            val filters = FakeEntityFactory.createMovieFilters()

            Mockito
                .`when`(repository.getMovieFilters())
                .thenReturn(filters)

            filters.forEach {
                Mockito
                    .`when`(repository.getMoviesByType(it.code, true))
                    .thenReturn(FakeEntityFactory.createFakeMovieList(size = 3))
            }

            val result = getAllMovies.executeAsync(Unit)

            verify(repository).getMovieFilters()

            filters.forEach {
                verify(repository).getMoviesByType(it.code, true)
            }

            assertTrue(result is Result.Success)

            val successResult = result as Result.Success

            assertTrue(successResult.result.isNotEmpty())
            assertEquals(MovieFilter.TYPE_NOW_PLAYING, successResult.result.first().filter.name)
        }
    }
}
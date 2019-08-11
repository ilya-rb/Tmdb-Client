package com.illiarb.tmdbclient.movies.home

import com.illiarb.tmdbclient.movies.domain.MoviesInteractorImpl
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.entity.MovieFilter
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.illiarb.tmdblcient.core.storage.MoviesRepository
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

/**
 * @author ilya-rb on 18.02.19.
 */
class MoviesInteractorTest {

    private val repository = mock<MoviesRepository>()
    private val errorHandler = mock<ErrorHandler>()

    private val moviesInteractor = MoviesInteractorImpl(repository, errorHandler)

    @Test
    fun `on movie details fetched images and reviews are requested`() {
        runBlocking {
            val movie = FakeEntityFactory.createFakeMovie()
            val expectedAppendToResponse = "images,reviews"

            val appendToResponseCaptor = argumentCaptor<String>()

            Mockito
                .`when`(repository.getMovieDetails(movie.id, expectedAppendToResponse))
                .thenReturn(movie)

            moviesInteractor.getMovieDetails(movie.id)

            verify(repository).getMovieDetails(eq(movie.id), appendToResponseCaptor.capture())

            Assert.assertEquals(expectedAppendToResponse, appendToResponseCaptor.firstValue)
        }
    }

    @Test
    fun `on movies fetched now playing section is first`() {
        runBlocking {
            val filters = FakeEntityFactory.createMovieFilters()

            Mockito
                .`when`(repository.getMovieFilters())
                .thenReturn(filters)

            filters.forEach {
                Mockito
                    .`when`(repository.getMoviesByType(it.code, false))
                    .thenReturn(FakeEntityFactory.createFakeMovieList(size = 3))
            }

            val result = moviesInteractor.getAllMovies()

            verify(repository).getMovieFilters()

            filters.forEach {
                verify(repository).getMoviesByType(it.code, false)
            }

            Assert.assertTrue(result is Result.Success)

            val successResult = result as Result.Success

            Assert.assertTrue(successResult.result.isNotEmpty())
            Assert.assertEquals(
                MovieFilter.TYPE_NOW_PLAYING,
                successResult.result.first().filter.name
            )
        }
    }
}
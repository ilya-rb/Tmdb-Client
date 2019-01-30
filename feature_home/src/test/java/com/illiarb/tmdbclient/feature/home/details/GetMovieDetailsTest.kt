package com.illiarb.tmdbclient.feature.home.details

import com.illiarb.tmdbclient.feature.home.details.domain.GetMovieDetails
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.exception.ErrorHandler
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

/**
 * @author ilya-rb on 28.01.19.
 */
class GetMovieDetailsTest {

    private val repository = mock<MoviesRepository>()
    private val errorHandler = mock<ErrorHandler>()

    private val getMovieDetails = GetMovieDetails(repository, errorHandler)

    @Test
    fun `on movie details fetched images and reviews are requested`() {
        runBlocking {
            val movie = FakeEntityFactory.createFakeMovie()
            val expectedAppendToResponse = "images,reviews"

            val appendToResponseCaptor = argumentCaptor<String>()

            Mockito
                .`when`(repository.getMovieDetails(movie.id, expectedAppendToResponse))
                .thenReturn(movie)

            getMovieDetails.executeAsync(movie.id)

            verify(repository).getMovieDetails(eq(movie.id), appendToResponseCaptor.capture())

            assertEquals(expectedAppendToResponse, appendToResponseCaptor.firstValue)
        }
    }
}
package com.illiarb.tmdbclient.feature.home.details

import com.illiarb.tmdbclient.feature.home.details.domain.GetMovieDetails
import com.illiarb.tmdbclient.feature.home.details.presentation.MovieDetailsModel
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdbcliient.core_test.runWithSubscription
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

/**
 * @author ilya-rb on 28.01.19.
 */
@ExperimentalCoroutinesApi
class MovieDetailsModelTest {

    private val getMovieDetails = mock<GetMovieDetails>()

    private val movieDetailsModel = MovieDetailsModel(getMovieDetails)

    @Test
    fun `on movie details fetch progress is showing`() {
        runBlocking {
            val movie = mockMovieDetails()

            runWithSubscription(movieDetailsModel.stateObservable()) { observer ->
                movieDetailsModel.getMovieDetails(movie.id)

                verify(getMovieDetails).executeAsync(movie.id)

                observer.withPrevious {
                    assertTrue(it.isLoading)
                }
            }
        }
    }

    @Test
    fun `on movie details fetched progress is hidden and details is shown`() {
        runBlocking {
            val movie = mockMovieDetails()

            runWithSubscription(movieDetailsModel.stateObservable()) { observer ->
                movieDetailsModel.getMovieDetails(movie.id)

                verify(getMovieDetails).executeAsync(movie.id)

                observer.withLatest {
                    assertFalse(it.isLoading)
                    assertEquals(movie, it.movie)
                }
            }
        }
    }

    private suspend fun mockMovieDetails(): Movie {
        val movie = FakeEntityFactory.createFakeMovie()

        Mockito
            .`when`(getMovieDetails.executeAsync(movie.id))
            .thenReturn(Result.Success(movie))

        return movie
    }
}
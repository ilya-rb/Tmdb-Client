package com.illiarb.tmdbclient.movies.home.details

import com.illiarb.tmdbclient.movies.details.MovieDetailsModel
import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdbcliient.core_test.runWithSubscription
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.MoviesInteractor
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

    private val moviesInteractor = mock<MoviesInteractor>()

    private val movieDetailsModel = MovieDetailsModel(moviesInteractor)

    @Test
    fun `on movie details fetch progress is showing`() {
        runBlocking {
            val movie = mockMovieDetails()

            runWithSubscription(movieDetailsModel.stateObservable()) { observer ->
                movieDetailsModel.getMovieDetails(movie.id)

                verify(moviesInteractor).getMovieDetails(movie.id)

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

                verify(moviesInteractor).getMovieDetails(movie.id)

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
            .`when`(moviesInteractor.getMovieDetails(movie.id))
            .thenReturn(Result.Success(movie))

        return movie
    }
}
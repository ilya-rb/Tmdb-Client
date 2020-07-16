package com.illiarb.tmdbclient.services.tmdb.interactor

import com.google.common.truth.Truth.assertThat
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Filter
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageUrlCreator
import com.illiarb.tmdbclient.services.tmdb.internal.interactor.DefaultMoviesInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.GenreMapper
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.MovieMapper
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.PersonMapper
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.ReviewMapper
import com.illiarb.tmdbclient.services.tmdb.internal.model.ResultsModel
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.DiscoverApi
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.MovieApi
import com.illiarb.tmdbclient.services.tmdb.internal.util.TmdbDateFormatter
import com.illiarb.tmdbclient.services.tmdb.repository.TestConfigurationRepository
import com.illiarb.tmdbclient.services.tmdb.repository.TestMovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class MoviesInteractorTest {

  private val discoverApi = mockk<DiscoverApi>()
  private val movieApi = mockk<MovieApi>()
  private val moviesRepository = TestMovieRepository()

  private val interactor = DefaultMoviesInteractor(
    moviesRepository,
    discoverApi,
    movieApi,
    MovieMapper(
      GenreMapper(),
      PersonMapper(),
      ReviewMapper(),
      ImageUrlCreator(),
      TmdbDateFormatter()
    ),
    TestConfigurationRepository(),
    TmdbDateFormatter()
  )

  @Test
  fun `it should not pass genre id if all genres are selected`() = runBlockingTest {
    coEvery {
      discoverApi.discoverMovies(any(), any())
    } returns Result.Ok(ResultsModel(emptyList(), 1, 1))

    interactor.discoverMovies(Filter.create(), 1)

    coVerify {
      discoverApi.discoverMovies(mapOf(), 1)
    }

    confirmVerified(discoverApi)
  }

  @Test
  fun `it should include images to response if change key is present`() = runBlockingTest {
    val details = interactor.getMovieDetails(movieId = 1).unwrap()
    assertThat(details.images).isNotEmpty()
  }
}
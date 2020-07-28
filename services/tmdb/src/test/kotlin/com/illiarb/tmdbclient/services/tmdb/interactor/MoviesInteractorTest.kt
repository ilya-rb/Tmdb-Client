package com.illiarb.tmdbclient.services.tmdb.interactor

import com.google.common.truth.Truth.assertThat
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageUrlCreator
import com.illiarb.tmdbclient.services.tmdb.internal.interactor.DefaultMoviesInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.GenreMapper
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.MovieMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.MovieApi
import com.illiarb.tmdbclient.services.tmdb.internal.util.TmdbDateFormatter
import com.illiarb.tmdbclient.services.tmdb.repository.TestConfigurationRepository
import com.illiarb.tmdbclient.services.tmdb.repository.TestMovieRepository
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class MoviesInteractorTest {

  private val movieApi = mockk<MovieApi>()
  private val moviesRepository = TestMovieRepository()

  private val interactor = DefaultMoviesInteractor(
    moviesRepository,
    movieApi,
    MovieMapper(
      GenreMapper(),
      PersonMapper(),
      ReviewMapper(),
      ImageUrlCreator(),
      TmdbDateFormatter()
    ),
    TestConfigurationRepository()
  )

  @Test
  fun `it should include images to response if change key is present`() = runBlockingTest {
    val details = interactor.getMovieDetails(movieId = 1).unwrap()
    assertThat(details.images).isNotEmpty()
  }
}
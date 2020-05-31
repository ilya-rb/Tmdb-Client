package com.illiarb.tmdbclient.services.tmdb.interactor

import com.google.common.truth.Truth.assertThat
import com.illiarb.tmdbclient.libs.test.tools.TestDispatcherProvider
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.Configuration
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageUrlCreator
import com.illiarb.tmdbclient.services.tmdb.internal.interactor.DefaultMoviesInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.DiscoverApi
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.MovieApi
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.GenreMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.MovieMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.PersonMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.ReviewMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.ResultsModel
import com.illiarb.tmdbclient.services.tmdb.internal.util.TmdbDateFormatter
import com.illiarb.tmdbclient.services.tmdb.repository.TestMovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class MoviesInteractorTest {

  private val cache = mockk<TmdbCache>()
  private val discoverApi = mockk<DiscoverApi>()
  private val movieApi = mockk<MovieApi>()
  private val moviesRepository = TestMovieRepository()

  private val interactor = DefaultMoviesInteractor(
    moviesRepository,
    discoverApi,
    movieApi,
    MovieMapper(GenreMapper(), PersonMapper(), ReviewMapper(), ImageUrlCreator(), TmdbDateFormatter()),
    cache,
    TestDispatcherProvider()
  )

  @Test
  fun `it should not pass genre id if all genres are selected`() = runBlockingTest {
    every {
      cache.getConfiguration()
    } returns Configuration(
      changeKeys = listOf("images")
    )

    coEvery {
      discoverApi.discoverMovies(any(), any())
    } returns Result.Ok(ResultsModel(emptyList(), 1, 1))

    interactor.discoverMovies(emptyList(), 1)

    coVerify {
      discoverApi.discoverMovies(null, 1)
    }

    confirmVerified(discoverApi)
  }

  @Test
  fun `it should include images to response if change key is present`() = runBlockingTest {
    every { cache.getConfiguration() } returns Configuration(
      changeKeys = listOf("images")
    )

    val details = interactor.getMovieDetails(movieId = 1).unwrap()
    assertThat(details.images).isNotEmpty()
  }
}
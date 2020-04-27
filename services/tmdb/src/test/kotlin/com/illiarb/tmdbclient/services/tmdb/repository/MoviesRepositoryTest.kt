package com.illiarb.tmdbclient.services.tmdb.repository

import com.illiarb.tmdbclient.libs.test.tools.TestResourceResolver
import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.configuration.Configuration
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.MovieApi
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.GenreMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.MovieMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.PersonMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.ReviewMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.MovieModel
import com.illiarb.tmdbclient.services.tmdb.internal.repository.DefaultMoviesRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class MoviesRepositoryTest {

  private val moviesApi = mockk<MovieApi>()
  private val cache = mockk<TmdbCache>()
  private val dispatcherProvider = mockk<DispatcherProvider>()

  private val repository = DefaultMoviesRepository(
    moviesApi,
    dispatcherProvider,
    cache,
    MovieMapper(
      GenreMapper(),
      PersonMapper(),
      ReviewMapper(),
      mockk()
    ),
    ReviewMapper(),
    TestResourceResolver()
  )

  @BeforeEach
  fun beforeAll() {
    every { dispatcherProvider.io } returns Dispatchers.Unconfined
  }

  @Test
  fun `should fetch movie details on io dispatcher from api`() = runBlockingTest {
    val id = 0
    val append = "append_to_response"

    repository.getMovieDetails(id, append)

    verify(exactly = 1) { dispatcherProvider.io }
    coVerify(exactly = 1) { moviesApi.getMovieDetails(id, append) }

    confirmVerified(dispatcherProvider, moviesApi)
  }

  @ParameterizedTest
  @ValueSource(strings = ["upcoming", "now_playing", "popular", "top_rated"])
  fun `should return cached data if it is not empty`(type: String) = runBlockingTest {
    every { cache.getMoviesByType(type) } returns listOf(MovieModel())
    every { cache.getConfiguration() } returns Configuration()

    repository.getMoviesByType(type)

    verify(exactly = 1) { cache.getMoviesByType(type) }
    verify(exactly = 1) { cache.getConfiguration() }
    coVerify(exactly = 0) { moviesApi.getMoviesByType(type) }

    confirmVerified(cache, moviesApi)
  }
}
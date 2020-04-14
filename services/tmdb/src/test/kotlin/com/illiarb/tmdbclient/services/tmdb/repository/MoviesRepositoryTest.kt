package com.illiarb.tmdbclient.services.tmdb.repository

import com.illiarb.tmdbclient.libs.test.tools.TestResourceResolver
import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.MovieApi
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.GenreMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.MovieMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.PersonMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.ReviewMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.MovieModel
import com.illiarb.tmdbclient.services.tmdb.internal.repository.DefaultMoviesRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class MoviesRepositoryTest {

  private val moviesApi = mock<MovieApi>()
  private val cache = mock<TmdbCache>()
  private val dispatcherProvider = mock<DispatcherProvider>()

  private val repository = DefaultMoviesRepository(
    moviesApi,
    dispatcherProvider,
    cache,
    MovieMapper(
      GenreMapper(),
      PersonMapper(),
      ReviewMapper(),
      mock()
    ),
    ReviewMapper(),
    TestResourceResolver()
  )

  @BeforeEach
  fun beforeAll() {
    whenever(dispatcherProvider.io).thenReturn(Dispatchers.Unconfined)
  }

  @Test
  fun `should fetch movie details on io dispatcher from api`() = runBlockingTest {
    val id = 0
    val append = "append_to_response"

    repository.getMovieDetails(id, append)

    verify(dispatcherProvider).io
    verifyNoMoreInteractions(dispatcherProvider)
    verifyZeroInteractions(cache)

    @Suppress("DeferredResultUnused")
    verify(moviesApi, times(1)).getMovieDetails(id, append)
  }

  @Test
  fun `should return cached data if it is not empty`() = runBlockingTest {
    whenever(cache.getMoviesByType(any())).thenReturn(
      listOf(
        MovieModel()
      )
    )

    val type = "upcoming"

    repository.getMoviesByType(type)

    verify(cache, times(1)).getMoviesByType(type)
    verifyZeroInteractions(moviesApi)
  }
}
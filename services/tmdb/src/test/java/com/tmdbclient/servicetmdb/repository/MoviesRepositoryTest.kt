package com.tmdbclient.servicetmdb.repository

import com.illiarb.tmdbclient.tools.DispatcherProvider
import com.illiarb.tmdbcliient.coretest.tools.TestResourceResolver
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.tmdbclient.servicetmdb.internal.cache.TmdbCache
import com.tmdbclient.servicetmdb.internal.network.api.MovieApi
import com.tmdbclient.servicetmdb.internal.network.mappers.GenreMapper
import com.tmdbclient.servicetmdb.internal.network.mappers.MovieMapper
import com.tmdbclient.servicetmdb.internal.network.mappers.PersonMapper
import com.tmdbclient.servicetmdb.internal.network.mappers.ReviewMapper
import com.tmdbclient.servicetmdb.internal.network.model.MovieModel
import com.tmdbclient.servicetmdb.internal.repository.DefaultMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
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

  @Before
  fun before() {
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
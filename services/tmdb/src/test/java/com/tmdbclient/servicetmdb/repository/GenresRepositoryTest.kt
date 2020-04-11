package com.tmdbclient.servicetmdb.repository

import com.illiarb.tmdbclient.util.Result
import com.illiarb.tmdbcliient.coretest.tools.TestDispatcherProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.tmdbclient.servicetmdb.internal.cache.TmdbCache
import com.tmdbclient.servicetmdb.internal.network.api.GenreApi
import com.tmdbclient.servicetmdb.internal.network.mappers.GenreMapper
import com.tmdbclient.servicetmdb.internal.network.model.GenreListModel
import com.tmdbclient.servicetmdb.internal.network.model.GenreModel
import com.tmdbclient.servicetmdb.internal.repository.DefaultGenresRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class GenresRepositoryTest {

  private val genresApi = mock<GenreApi>()
  private val cache = mock<TmdbCache>()
  private val repository = DefaultGenresRepository(
    genresApi,
    cache,
    TestDispatcherProvider(),
    GenreMapper()
  )

  @Test
  fun `should check cache first and return cached data if not empty`() = runBlockingTest {
    whenever(cache.getGenres()).thenReturn(
      listOf(
        GenreModel(),
        GenreModel()
      )
    )

    val result = repository.getGenres()

    verify(cache, times(1)).getGenres()
    verifyZeroInteractions(genresApi)

    assertTrue(result is Result.Ok && result.data.isNotEmpty())
  }

  @Test
  fun `should fetch data from api if cache is empty`() = runBlockingTest {
    whenever(cache.getGenres()).thenReturn(emptyList())

    repository.getGenres()

    verify(cache, times(1)).getGenres()

    @Suppress("DeferredResultUnused")
    verify(genresApi).getGenres()
  }

  @Test
  fun `should store genres in cache after successful fetch from api`() = runBlockingTest {
    val apiGenres = GenreListModel()

    whenever(cache.getGenres()).thenReturn(emptyList())
    whenever(genresApi.getGenres()).thenReturn(Result.Ok(apiGenres))

    repository.getGenres()

    verify(cache, times(1)).storeGenres(any())
  }
}
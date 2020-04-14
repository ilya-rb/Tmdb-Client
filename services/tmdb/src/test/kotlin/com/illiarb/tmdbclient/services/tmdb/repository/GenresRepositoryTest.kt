package com.illiarb.tmdbclient.services.tmdb.repository

import com.google.common.truth.Truth.assertThat
import com.illiarb.tmdbclient.libs.test.tools.TestDispatcherProvider
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.GenreApi
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.GenreMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.GenreListModel
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.GenreModel
import com.illiarb.tmdbclient.services.tmdb.internal.repository.DefaultGenresRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

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

    assertThat(result).isInstanceOf(Result.Ok::class.java)
    assertThat(result.unwrap()).isNotEmpty()
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
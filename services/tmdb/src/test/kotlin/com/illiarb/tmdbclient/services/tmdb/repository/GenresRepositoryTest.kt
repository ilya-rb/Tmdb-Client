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
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class GenresRepositoryTest {

  private val genresApi = mockk<GenreApi>()
  private val cache = mockk<TmdbCache>()
  private val repository = DefaultGenresRepository(
    genresApi,
    cache,
    TestDispatcherProvider(),
    GenreMapper()
  )

  @Test
  fun `should check cache first and return cached data if not empty`() = runBlockingTest {
    every { cache.getGenres() } returns listOf(GenreModel(), GenreModel())

    val result = repository.getGenres()
    verify(exactly = 1) { cache.getGenres() }

    confirmVerified(cache)

    assertThat(result).isInstanceOf(Result.Ok::class.java)
    assertThat(result.unwrap()).isNotEmpty()
  }

  @Test
  fun `should fetch data from api if cache is empty`() = runBlockingTest {
    every { cache.getGenres() } returns emptyList()

    repository.getGenres()

    verify(exactly = 1) { cache.getGenres() }

    @Suppress("DeferredResultUnused")
    coVerify { genresApi.getGenres() }

    confirmVerified(cache, genresApi)
  }

  @Test
  fun `should store genres in cache after successful fetch from api`() = runBlockingTest {
    val apiGenres = GenreListModel()

    every { cache.getGenres() } returns emptyList()
    coEvery { genresApi.getGenres() } returns Result.Ok(apiGenres)

    repository.getGenres()

    verify(exactly = 1) { cache.storeGenres(any()) }
    verify(exactly = 1) { cache.getGenres() }

    coVerify { genresApi.getGenres() }

    confirmVerified(cache, genresApi)
  }
}
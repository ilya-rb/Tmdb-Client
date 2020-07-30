package com.illiarb.tmdbclient.services.tmdb.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Filter
import com.illiarb.tmdbclient.services.tmdb.internal.dto.ResultsDto
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageUrlCreator
import com.illiarb.tmdbclient.services.tmdb.internal.interactor.DefaultDiscoverInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.GenreMapper
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.MovieMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.DiscoverApi
import com.illiarb.tmdbclient.services.tmdb.internal.util.TmdbDateFormatter
import com.illiarb.tmdbclient.services.tmdb.repository.TestConfigurationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class DiscoverInteractorTest {

  private val discoverApi = mockk<DiscoverApi>()
  private val interactor = DefaultDiscoverInteractor(
    TestConfigurationRepository(),
    TmdbDateFormatter(),
    discoverApi,
    MovieMapper(
      GenreMapper(),
      ImageUrlCreator(),
      TmdbDateFormatter()
    )
  )

  @Test
  fun `it should not pass genre id if all genres are selected`() = runBlockingTest {
    coEvery {
      discoverApi.discoverMovies(any(), any())
    } returns Result.Ok(ResultsDto(emptyList(), 1, 1))

    interactor.discoverMovies(Filter.create(), 1)

    coVerify {
      discoverApi.discoverMovies(mapOf(), 1)
    }

    confirmVerified(discoverApi)
  }
}
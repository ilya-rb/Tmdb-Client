package com.illiarb.tmdbclient.services.tmdb.repository

import com.illiarb.tmdbclient.libs.test.tools.TestDispatcherProvider
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageConfig
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.CountryMapper
import com.illiarb.tmdbclient.services.tmdb.internal.model.Configuration
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.ConfigurationApi
import com.illiarb.tmdbclient.services.tmdb.internal.repository.DefaultConfigurationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class ConfigurationRepositoryTest {

  private val cache = mockk<TmdbCache>()
  private val api = mockk<ConfigurationApi>()
  private val repository = DefaultConfigurationRepository(
    cache,
    api,
    TestDispatcherProvider(),
    CountryMapper()
  )

  @Test
  fun `it should check cache first and return from cached data if not empty`() = runBlockingTest {
    every { cache.getConfiguration() } returns
        Configuration(
          changeKeys = listOf("images", "videos"),
          images = ImageConfig(
            secureBaseUrl = "https://api.com/",
            backdropSizes = listOf("w500"),
            posterSizes = listOf("w500"),
            profileSizes = listOf("w500")
          )
        )

    every { cache.getConfigurationLastUpdateTimestamp() } returns System.currentTimeMillis()

    repository.getConfiguration()

    verify(exactly = 1) { cache.getConfiguration() }
    verify { cache.getConfigurationLastUpdateTimestamp() }
    coVerify(exactly = 0) { api.getConfiguration() }

    confirmVerified(cache, api)
  }

  @Test
  fun `it should check cache and if it is empty fetch from api`() = runBlockingTest {
    every { cache.getConfiguration() } returns Configuration()
    every { cache.getConfigurationLastUpdateTimestamp() } returns System.currentTimeMillis()

    repository.getConfiguration()

    verify(exactly = 1) { cache.getConfiguration() }
    verify { cache.getConfigurationLastUpdateTimestamp() }
    coVerify(exactly = 1) { api.getConfiguration() }

    confirmVerified(api, cache)
  }

  @Test
  fun `it should store configuration in cache after successful fetch`() = runBlockingTest {
    val configToStore =
      Configuration()

    every { cache.getConfiguration() } returns Configuration()
    every { cache.getConfigurationLastUpdateTimestamp() } returns System.currentTimeMillis()

    coEvery { api.getConfiguration() } returns Result.Ok(configToStore)

    repository.getConfiguration()

    verify { cache.storeConfiguration(configToStore) }
    verify { cache.getConfiguration() }
    verify { cache.getConfigurationLastUpdateTimestamp() }

    confirmVerified(cache)
  }
}
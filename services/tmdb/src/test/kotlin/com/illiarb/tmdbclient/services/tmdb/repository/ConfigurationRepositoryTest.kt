package com.illiarb.tmdbclient.services.tmdb.repository

import com.illiarb.tmdbclient.libs.test.tools.TestDispatcherProvider
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.configuration.Configuration
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageConfig
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.ConfigurationApi
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.CountryMapper
import com.illiarb.tmdbclient.services.tmdb.internal.repository.DefaultConfigurationRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class ConfigurationRepositoryTest {

  private val cache = mock<TmdbCache>()
  private val api = mock<ConfigurationApi>()
  private val repository = DefaultConfigurationRepository(
    cache,
    api,
    TestDispatcherProvider(),
    CountryMapper()
  )

  @Test
  fun `it should check cache first and return from cached data if not empty`() = runBlockingTest {
    whenever(cache.getConfiguration())
      .thenReturn(
        Configuration(
          images = ImageConfig(
            secureBaseUrl = "some_url",
            backdropSizes = listOf("size"),
            posterSizes = listOf("size"),
            profileSizes = listOf("size")
          ),
          changeKeys = listOf("key_1", "key_2")
        )
      )

    repository.getConfiguration()

    verify(cache, times(1)).getConfiguration()
    verifyZeroInteractions(api)
  }

  @Test
  fun `it should check cache and if it is empty fetch from api`() = runBlockingTest {
    whenever(cache.getConfiguration()).thenReturn(Configuration())

    repository.getConfiguration()

    verify(cache, times(1)).getConfiguration()
    verify(api).getConfiguration()
  }

  @Test
  fun `it should store configuration in cache after successful fetch`() = runBlockingTest {
    val configToStore = Configuration()

    whenever(cache.getConfiguration()).thenReturn(Configuration())
    whenever(api.getConfiguration()).thenReturn(Result.Ok(configToStore))

    repository.getConfiguration()

    verify(cache, times(1)).storeConfiguration(configToStore)
  }
}
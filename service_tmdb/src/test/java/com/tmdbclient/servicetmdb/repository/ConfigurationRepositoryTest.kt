package com.tmdbclient.servicetmdb.repository

import com.illiarb.tmdbcliient.coretest.TestDependencyProvider
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.tmdbclient.servicetmdb.api.ConfigurationApi
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.configuration.Configuration
import com.tmdbclient.servicetmdb.configuration.ImageConfig
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class ConfigurationRepositoryTest {

    private val cache = mock<TmdbCache>()
    private val api = mock<ConfigurationApi>()
    private val repository = DefaultConfigurationRepository(
        cache,
        api,
        TestDependencyProvider.provideDispatcherProvider()
    )

    @Test
    fun `should check cache first and return from cached data if not empty`() = runBlockingTest {
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
    fun `should check cache and if it is empty fetch from api`() = runBlockingTest {
        whenever(cache.getConfiguration()).thenReturn(Configuration())

        repository.getConfiguration()

        verify(cache, times(1)).getConfiguration()

        @Suppress("DeferredResultUnused")
        verify(api).getConfiguration()
    }

    @Test
    fun `should store configuration in cache after successful fetch`() = runBlockingTest {
        val configToStore = Configuration()

        whenever(cache.getConfiguration()).thenReturn(Configuration())
        whenever(api.getConfiguration()).thenReturn(CompletableDeferred(configToStore))

        repository.getConfiguration()

        verify(cache, times(1)).storeConfiguration(configToStore)
    }
}
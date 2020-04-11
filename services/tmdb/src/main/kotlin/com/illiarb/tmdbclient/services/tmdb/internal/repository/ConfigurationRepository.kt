package com.illiarb.tmdbclient.services.tmdb.internal.repository

import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.services.tmdb.domain.Country
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.ConfigurationApi
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.configuration.Configuration
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.CountryMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

internal interface ConfigurationRepository {

  suspend fun getConfiguration(refresh: Boolean = false): Result<Configuration>

  suspend fun getCountries(): Result<List<Country>>
}

@Singleton
internal class DefaultConfigurationRepository @Inject constructor(
  private val cache: TmdbCache,
  private val api: ConfigurationApi,
  private val dispatcherProvider: DispatcherProvider,
  private val countryMapper: CountryMapper
) : ConfigurationRepository {

  override suspend fun getConfiguration(refresh: Boolean): Result<Configuration> = Result.create {
    if (refresh) {
      val configuration = api.getConfiguration().unwrap()
      cache.storeConfiguration(configuration)
      return@create cache.getConfiguration()
    }

    val cached = withContext(dispatcherProvider.io) { cache.getConfiguration() }
    if (cached.isNotEmpty()) {
      cached
    } else {
      val configuration = api.getConfiguration().unwrap()
      cache.storeConfiguration(configuration)
      withContext(dispatcherProvider.io) { cache.getConfiguration() }
    }
  }

  override suspend fun getCountries(): Result<List<Country>> = Result.create {
    val cached = withContext(dispatcherProvider.io) { cache.getCountries() }
    if (cached.isNotEmpty()) {
      countryMapper.mapList(cached)
    } else {
      val countries = api.getCountries().unwrap()
      cache.storeCountries(countries)

      val result = withContext(dispatcherProvider.io) {
        cache.getCountries()
      }
      countryMapper.mapList(result)
    }
  }
}
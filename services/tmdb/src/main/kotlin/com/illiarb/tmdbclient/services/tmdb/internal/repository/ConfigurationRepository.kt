package com.illiarb.tmdbclient.services.tmdb.internal.repository

import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Country
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.ConfigurationApi
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.CountryMapper
import com.illiarb.tmdbclient.services.tmdb.internal.model.Configuration
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal interface ConfigurationRepository {

  suspend fun getConfiguration(refresh: Boolean = false): Result<Configuration>

  suspend fun getCountries(): Result<List<Country>>
}

internal class DefaultConfigurationRepository @Inject constructor(
  private val cache: TmdbCache,
  private val api: ConfigurationApi,
  private val dispatcherProvider: DispatcherProvider,
  private val countryMapper: CountryMapper
) : ConfigurationRepository {

  companion object {
    const val CONFIGURATION_EXPIRY_DAYS = 2
  }

  override suspend fun getConfiguration(refresh: Boolean): Result<Configuration> = Result.create {
    if (refresh || isConfigurationExpired()) {
      val configuration = api.getConfiguration().unwrap()
      cache.storeConfiguration(configuration)
      cache.updateConfigurationTimestamp(System.currentTimeMillis())
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

  private fun isConfigurationExpired(): Boolean {
    val configurationLastUpdated = cache.getConfigurationLastUpdateTimestamp()
    return TimeUnit.MILLISECONDS.toDays(configurationLastUpdated) > CONFIGURATION_EXPIRY_DAYS
  }
}
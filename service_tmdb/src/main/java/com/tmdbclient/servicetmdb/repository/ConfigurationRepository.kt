package com.tmdbclient.servicetmdb.repository

import com.illiarb.tmdblcient.core.domain.Country
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.api.ConfigurationApi
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.configuration.Configuration
import com.tmdbclient.servicetmdb.mappers.CountryMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface ConfigurationRepository {

    suspend fun getConfiguration(refresh: Boolean = false): Result<Configuration>

    suspend fun getCountries(): Result<List<Country>>
}

@Singleton
class DefaultConfigurationRepository @Inject constructor(
    private val cache: TmdbCache,
    private val api: ConfigurationApi,
    private val dispatcherProvider: DispatcherProvider,
    private val countryMapper: CountryMapper
) : ConfigurationRepository {

    override suspend fun getConfiguration(refresh: Boolean): Result<Configuration> = Result.create {
        if (refresh) {
            val configuration = api.getConfigurationAsync().await()
            cache.storeConfiguration(configuration)
            return@create cache.getConfiguration()
        }

        val cached = withContext(dispatcherProvider.io) { cache.getConfiguration() }
        if (cached.isNotEmpty()) {
            cached
        } else {
            val configuration = api.getConfigurationAsync().await()
            cache.storeConfiguration(configuration)
            cache.getConfiguration()
        }
    }

    override suspend fun getCountries(): Result<List<Country>> = Result.create {
        val cached = withContext(dispatcherProvider.io) { cache.getCountries() }
        if (cached.isNotEmpty()) {
            countryMapper.mapList(cached)
        } else {
            val countries = api.getCountriesAsync().await()
            cache.storeCountries(countries)

            val result = withContext(dispatcherProvider.io) {
                cache.getCountries()
            }
            countryMapper.mapList(result)
        }
    }
}
package com.tmdbclient.servicetmdb.repository

import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.api.ConfigurationApi
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.configuration.Configuration
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ConfigurationRepository {

    suspend fun getConfiguration(): Result<Configuration>
}

class DefaultConfigurationRepository @Inject constructor(
    private val cache: TmdbCache,
    private val api: ConfigurationApi,
    private val dispatcherProvider: DispatcherProvider
) : ConfigurationRepository {

    override suspend fun getConfiguration(): Result<Configuration> = Result.create {
        val cached = withContext(dispatcherProvider.io) { cache.getConfiguration() }

        if (cached.isNotEmpty()) {
            cached
        } else {
            val configuration = api.getConfiguration().await()
            cache.storeConfiguration(configuration)
            cache.getConfiguration()
        }
    }
}
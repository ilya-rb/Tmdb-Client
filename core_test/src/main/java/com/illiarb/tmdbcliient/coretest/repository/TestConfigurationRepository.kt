package com.illiarb.tmdbcliient.coretest.repository

import com.illiarb.tmdblcient.core.domain.Country
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.configuration.Configuration
import com.tmdbclient.servicetmdb.repository.ConfigurationRepository

class TestConfigurationRepository : ConfigurationRepository {

    override suspend fun getConfiguration(refresh: Boolean): Result<Configuration> {
        return Result.Success(Configuration())
    }

    override suspend fun getCountries(): Result<List<Country>> {
        return Result.Success(emptyList())
    }
}
package com.illiarb.tmdbcliient.coretest.repository

import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.configuration.Configuration
import com.tmdbclient.servicetmdb.repository.ConfigurationRepository

class TestConfigurationRepository : ConfigurationRepository {

    override suspend fun getConfiguration(): Result<Configuration> {
        return Result.Success(Configuration())
    }
}
package com.illiarb.tmdbclient.services.tmdb.repository

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Country
import com.illiarb.tmdbclient.services.tmdb.internal.model.Configuration
import com.illiarb.tmdbclient.services.tmdb.internal.repository.ConfigurationRepository

internal class TestConfigurationRepository : ConfigurationRepository {

  override suspend fun getConfiguration(refresh: Boolean): Result<Configuration> {
    return Result.Ok(Configuration(changeKeys = listOf("images")))
  }

  override suspend fun getCountries(): Result<List<Country>> {
    return Result.Ok(emptyList())
  }
}
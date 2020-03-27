package com.tmdbclient.servicetmdb.api

import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.configuration.Configuration
import com.tmdbclient.servicetmdb.model.CountryModel
import retrofit2.http.GET

interface ConfigurationApi {

  @GET("configuration")
  suspend fun getConfiguration(): Result<Configuration>

  @GET("configuration/countries")
  suspend fun getCountries(): Result<List<CountryModel>>
}
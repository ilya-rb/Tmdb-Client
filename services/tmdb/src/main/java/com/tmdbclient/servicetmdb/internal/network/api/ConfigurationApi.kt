package com.tmdbclient.servicetmdb.internal.network.api

import com.tmdbclient.servicetmdb.internal.configuration.Configuration
import com.tmdbclient.servicetmdb.internal.network.model.CountryModel
import com.illiarb.tmdbclient.util.Result
import retrofit2.http.GET

internal interface ConfigurationApi {

  @GET("configuration")
  suspend fun getConfiguration(): Result<Configuration>

  @GET("configuration/countries")
  suspend fun getCountries(): Result<List<CountryModel>>
}
package com.tmdbclient.servicetmdb.api

import com.tmdbclient.servicetmdb.configuration.Configuration
import com.tmdbclient.servicetmdb.model.CountryModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ConfigurationApi {

  @GET("configuration")
  fun getConfigurationAsync(): Deferred<Configuration>

  @GET("configuration/countries")
  fun getCountriesAsync(): Deferred<List<CountryModel>>
}
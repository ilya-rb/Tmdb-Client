package com.illiarb.tmdbclient.services.tmdb.internal.network.api

import com.illiarb.tmdbclient.services.tmdb.internal.configuration.Configuration
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.CountryModel
import com.illiarb.tmdbclient.libs.util.Result
import retrofit2.http.GET

internal interface ConfigurationApi {

  @GET("configuration")
  suspend fun getConfiguration(): Result<Configuration>

  @GET("configuration/countries")
  suspend fun getCountries(): Result<List<CountryModel>>
}
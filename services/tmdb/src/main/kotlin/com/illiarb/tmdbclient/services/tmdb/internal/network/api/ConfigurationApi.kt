package com.illiarb.tmdbclient.services.tmdb.internal.network.api

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.internal.dto.ConfigurationDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.CountryDto
import retrofit2.http.GET

internal interface ConfigurationApi {

  @GET("configuration")
  suspend fun getConfiguration(): Result<ConfigurationDto>

  @GET("configuration/countries")
  suspend fun getCountries(): Result<List<CountryDto>>
}
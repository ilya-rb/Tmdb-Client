package com.illiarb.tmdbclient.services.tmdb.internal.network.api

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.internal.dto.MovieDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.ResultsDto
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

internal interface DiscoverApi {

  @GET("discover/movie")
  suspend fun discoverMovies(
    @QueryMap(encoded = true) filters: Map<String, String>,
    @Query("page") page: Int
  ): Result<ResultsDto<MovieDto>>
}
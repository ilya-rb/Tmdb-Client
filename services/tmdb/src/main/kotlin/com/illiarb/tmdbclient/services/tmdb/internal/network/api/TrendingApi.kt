package com.illiarb.tmdbclient.services.tmdb.internal.network.api

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.internal.dto.ResultsDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.TrendingDto
import retrofit2.http.GET
import retrofit2.http.Path

internal interface TrendingApi {

  companion object {
    const val TRENDING_THIS_WEEK = "week"
    const val TRENDING_TYPE_MOVIES = "movie"
  }

  @GET("trending/{media_type}/{time_window}")
  suspend fun getTrendingAsync(
    @Path("media_type") mediaType: String,
    @Path("time_window") timeWindow: String
  ): Result<ResultsDto<TrendingDto>>
}
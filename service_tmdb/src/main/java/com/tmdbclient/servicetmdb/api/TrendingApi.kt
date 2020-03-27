package com.tmdbclient.servicetmdb.api

import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.model.ResultsModel
import com.tmdbclient.servicetmdb.model.TrendingModel
import retrofit2.http.GET
import retrofit2.http.Path

interface TrendingApi {

  companion object {
    const val TRENDING_THIS_WEEK = "week"
    const val TRENDING_TYPE_MOVIES = "movie"
  }

  @GET("trending/{media_type}/{time_window}")
  suspend fun getTrendingAsync(
    @Path("media_type") mediaType: String,
    @Path("time_window") timeWindow: String
  ): Result<ResultsModel<TrendingModel>>
}
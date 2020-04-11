package com.tmdbclient.servicetmdb.internal.network.api

import com.illiarb.tmdbclient.util.Result
import com.tmdbclient.servicetmdb.internal.network.model.MovieModel
import com.tmdbclient.servicetmdb.internal.network.model.ResultsModel
import retrofit2.http.GET
import retrofit2.http.Query

internal interface DiscoverApi {

  @GET("discover/movie")
  suspend fun discoverMovies(@Query("with_genres") genres: String? = null): Result<ResultsModel<MovieModel>>
}
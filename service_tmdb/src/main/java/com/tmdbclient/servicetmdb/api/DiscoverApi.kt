package com.tmdbclient.servicetmdb.api

import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.model.MovieModel
import com.tmdbclient.servicetmdb.model.ResultsModel
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverApi {

  @GET("discover/movie")
  suspend fun discoverMovies(@Query("with_genres") genres: String? = null): Result<ResultsModel<MovieModel>>
}
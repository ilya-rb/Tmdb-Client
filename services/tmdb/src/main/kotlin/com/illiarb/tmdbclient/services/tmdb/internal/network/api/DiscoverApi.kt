package com.illiarb.tmdbclient.services.tmdb.internal.network.api

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.MovieModel
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.ResultsModel
import retrofit2.http.GET
import retrofit2.http.Query

internal interface DiscoverApi {

  @GET("discover/movie")
  suspend fun discoverMovies(
    @Query("with_genres") genres: String? = null,
    @Query("page") page: Int
  ): Result<ResultsModel<MovieModel>>
}
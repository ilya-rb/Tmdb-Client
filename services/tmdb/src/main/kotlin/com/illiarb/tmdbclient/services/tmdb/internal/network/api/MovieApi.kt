package com.illiarb.tmdbclient.services.tmdb.internal.network.api

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Video
import com.illiarb.tmdbclient.services.tmdb.internal.dto.MovieDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.ResultsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface MovieApi {

  @GET("movie/{type}")
  suspend fun getMoviesByType(@Path("type") type: String): Result<ResultsDto<MovieDto>>

  @GET("movie/{id}")
  suspend fun getMovieDetails(
    @Path("id") id: Int,
    @Query("append_to_response") appendToResponse: String
  ): Result<MovieDto>

  @GET("movie/{id}/similar")
  suspend fun getSimilarMovies(@Path("id") id: Int): Result<ResultsDto<MovieDto>>

  @GET("movie/{id}/videos")
  suspend fun getMovieVideos(@Path("id") id: Int): Result<ResultsDto<Video>>

  @GET("search/movie")
  suspend fun searchMovies(@Query("query") query: String) : Result<ResultsDto<MovieDto>>
}
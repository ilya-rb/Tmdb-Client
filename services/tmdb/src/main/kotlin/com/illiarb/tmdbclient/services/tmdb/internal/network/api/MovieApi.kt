package com.illiarb.tmdbclient.services.tmdb.internal.network.api

import com.illiarb.tmdbclient.services.tmdb.domain.Video
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.internal.model.MovieModel
import com.illiarb.tmdbclient.services.tmdb.internal.model.ResultsModel
import com.illiarb.tmdbclient.services.tmdb.internal.model.ReviewModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface MovieApi {

  @GET("movie/{type}")
  suspend fun getMoviesByType(@Path("type") type: String): Result<ResultsModel<MovieModel>>

  @GET("movie/{id}")
  suspend fun getMovieDetails(
    @Path("id") id: Int,
    @Query("append_to_response") appendToResponse: String
  ): Result<MovieModel>

  @GET("movie/{id}/similar")
  suspend fun getSimilarMovies(@Path("id") id: Int): Result<ResultsModel<MovieModel>>

  @GET("movie/{id}/reviews")
  suspend fun getMovieReviews(@Path("id") id: Int): Result<ResultsModel<ReviewModel>>

  @GET("movie/{id}/videos")
  suspend fun getMovieVideos(@Path("id") id: Int): Result<ResultsModel<Video>>
}
package com.tmdbclient.servicetmdb.api

import com.illiarb.tmdblcient.core.domain.Video
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.model.MovieModel
import com.tmdbclient.servicetmdb.model.ResultsModel
import com.tmdbclient.servicetmdb.model.ReviewModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

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
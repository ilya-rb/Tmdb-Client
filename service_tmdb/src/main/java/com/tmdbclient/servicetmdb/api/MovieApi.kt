package com.tmdbclient.servicetmdb.api

import com.illiarb.tmdblcient.core.domain.Video
import com.tmdbclient.servicetmdb.model.MovieModel
import com.tmdbclient.servicetmdb.model.ResultsModel
import com.tmdbclient.servicetmdb.model.ReviewModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

  @GET("movie/{type}")
  fun getMoviesByTypeAsync(@Path("type") type: String): Deferred<ResultsModel<MovieModel>>

  @GET("movie/{id}")
  fun getMovieDetailsAsync(
    @Path("id") id: Int,
    @Query("append_to_response") appendToResponse: String
  ): Deferred<MovieModel>

  @GET("movie/{id}/similar")
  fun getSimilarMoviesAsync(@Path("id") id: Int): Deferred<ResultsModel<MovieModel>>

  @GET("movie/{id}/reviews")
  fun getMovieReviewsAsync(@Path("id") id: Int): Deferred<ResultsModel<ReviewModel>>

  @GET("movie/{id}/videos")
  fun getMovieVideosAsync(@Path("id") id: Int): Deferred<ResultsModel<Video>>
}
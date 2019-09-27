package com.tmdbclient.service_tmdb.api

import com.tmdbclient.service_tmdb.model.MovieModel
import com.tmdbclient.service_tmdb.model.ResultsModel
import com.tmdbclient.service_tmdb.model.ReviewModel
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

    @GET("movie/{id}/reviews")
    fun getMovieReviewsAsync(@Path("id") id: Int): Deferred<ResultsModel<ReviewModel>>
}
package com.illiarb.tmdbclient.storage.network.api.service

import com.illiarb.tmdbclient.storage.model.MovieModel
import com.illiarb.tmdbclient.storage.model.ResultsModel
import com.illiarb.tmdbclient.storage.model.ReviewModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/{type}")
    fun getMoviesByType(@Path("type") type: String): Deferred<ResultsModel<MovieModel>>

    @GET("movie/{id}")
    fun getMovieDetails(
        @Path("id") id: Int,
        @Query("append_to_response") appendToResponse: String
    ): Deferred<MovieModel>

    @GET("movie/{id}/reviews")
    fun getMovieReviews(@Path("id") id: Int): Deferred<ResultsModel<ReviewModel>>
}
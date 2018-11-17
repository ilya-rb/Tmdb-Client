package com.illiarb.tmdbclient.storage.network.api

import com.illiarb.tmdbclient.storage.model.MovieModel
import com.illiarb.tmdbclient.storage.model.ResultsModel
import com.illiarb.tmdbclient.storage.model.ReviewModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/{type}")
    fun getMoviesByType(@Path("type") type: String): Single<ResultsModel<MovieModel>>

    @GET("movie/{id}")
    fun getMovieDetails(
        @Path("id") id: Int,
        @Query("append_to_response") appendToResponse: String
    ): Single<MovieModel>

    @GET("movie/{id}/reviews")
    fun getMovieReviews(@Path("id") id: Int): Single<ResultsModel<ReviewModel>>
}
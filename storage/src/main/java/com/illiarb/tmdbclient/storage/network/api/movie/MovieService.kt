package com.illiarb.tmdbclient.storage.network.api.movie

import com.illiarb.tmdbclient.storage.dto.MovieDto
import com.illiarb.tmdbclient.storage.dto.ResultsDto
import com.illiarb.tmdbclient.storage.dto.ReviewDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/{type}")
    fun getMoviesByType(@Path("type") type: String): Single<ResultsDto<MovieDto>>

    @GET("movie/{id}")
    fun getMovieDetails(
        @Path("id") id: Int,
        @Query("append_to_response") appendToResponse: String
    ): Single<MovieDto>

    @GET("movie/{id}/reviews")
    fun getMovieReviews(@Path("id") id: Int): Single<ResultsDto<ReviewDto>>
}
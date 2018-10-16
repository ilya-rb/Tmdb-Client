package com.illiarb.tmdbclient.network.api.movie

import com.illiarb.tmdbclient.network.responses.MovieResponse
import com.illiarb.tmdbclient.network.responses.ResultsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/{type}")
    fun getMoviesByType(@Path("type") type: String): Single<ResultsResponse<MovieResponse>>

    @GET("movie/{id}")
    fun getMovieDetails(
        @Path("id") id: Int,
        @Query("append_to_response") appendToResponse: String
    ): Single<MovieResponse>
}
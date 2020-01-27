package com.tmdbclient.servicetmdb.api

import com.tmdbclient.servicetmdb.model.MovieModel
import com.tmdbclient.servicetmdb.model.ResultsModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DiscoverApi {

    @GET("discover/movie")
    @Headers(ApiHeaders.HEADER_SUPPORTS_REGION)
    fun discoverMoviesAsync(@Query("with_genres") genres: String? = null): Deferred<ResultsModel<MovieModel>>
}
package com.tmdbclient.service_tmdb.api

import com.tmdbclient.service_tmdb.model.MovieModel
import com.tmdbclient.service_tmdb.model.ResultsModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverApi {

    @GET("discover/movie")
    fun discoverMoviesAsync(@Query("with_genres") genres: String? = null): Deferred<ResultsModel<MovieModel>>
}
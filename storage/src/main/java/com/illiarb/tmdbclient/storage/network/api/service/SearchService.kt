package com.illiarb.tmdbclient.storage.network.api.service

import com.illiarb.tmdbclient.storage.model.MovieModel
import com.illiarb.tmdbclient.storage.model.ResultsModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author ilya-rb on 27.12.18.
 */
interface SearchService {

    @GET("search/movie")
    fun searchMoviesAsync(@Query("query") query: String): Deferred<ResultsModel<MovieModel>>
}
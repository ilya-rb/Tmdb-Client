package com.tmdbclient.servicetmdb.api

import com.tmdbclient.servicetmdb.model.ResultsModel
import com.tmdbclient.servicetmdb.model.TrendingModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface TrendingApi {

    companion object {
        const val TRENDING_THIS_WEEK = "week"
        const val TRENDING_TYPE_MOVIES = "movie"
    }

    @GET("trending/{media_type}/{time_window}")
    fun getTrendingAsync(
        @Path("media_type") mediaType: String,
        @Path("time_window") timeWindow: String
    ): Deferred<ResultsModel<TrendingModel>>
}
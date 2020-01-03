package com.tmdbclient.service_tmdb.api

import com.tmdbclient.service_tmdb.model.ResultsModel
import com.tmdbclient.service_tmdb.model.TrendingModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface TrendingApi {

    companion object {
        const val TRENDING_THIS_WEEK = "week"
        const val TRENDING_TYPE_ALL = "all"
    }

    @GET("trending/{media_type}/{time_window}")
    fun getTrendingAsync(
        @Path("media_type") mediaType: String,
        @Path("time_window") timeWindow: String
    ): Deferred<ResultsModel<TrendingModel>>
}
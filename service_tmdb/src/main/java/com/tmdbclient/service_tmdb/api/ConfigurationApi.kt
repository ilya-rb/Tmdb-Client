package com.tmdbclient.service_tmdb.api

import com.tmdbclient.service_tmdb.configuration.Configuration
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ConfigurationApi {

    @GET("configuration")
    fun getConfiguration(): Deferred<Configuration>
}
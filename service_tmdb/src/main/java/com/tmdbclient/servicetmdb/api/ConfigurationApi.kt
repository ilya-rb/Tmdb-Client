package com.tmdbclient.servicetmdb.api

import com.tmdbclient.servicetmdb.configuration.Configuration
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ConfigurationApi {

    @GET("configuration")
    fun getConfiguration(): Deferred<Configuration>
}
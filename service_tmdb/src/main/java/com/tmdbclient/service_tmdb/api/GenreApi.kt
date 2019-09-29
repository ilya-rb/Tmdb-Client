package com.tmdbclient.service_tmdb.api

import com.tmdbclient.service_tmdb.model.GenreListModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface GenreApi {

    @GET("genre/movie/list")
    fun getGenresAsync(): Deferred<GenreListModel>
}
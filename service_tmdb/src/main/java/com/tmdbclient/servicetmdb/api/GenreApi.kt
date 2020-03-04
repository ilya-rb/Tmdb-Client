package com.tmdbclient.servicetmdb.api

import com.tmdbclient.servicetmdb.model.GenreListModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface GenreApi {

  @GET("genre/movie/list")
  fun getGenresAsync(): Deferred<GenreListModel>
}
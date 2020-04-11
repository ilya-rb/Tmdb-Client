package com.tmdbclient.servicetmdb.internal.network.api

import com.illiarb.tmdbclient.util.Result
import com.tmdbclient.servicetmdb.internal.network.model.GenreListModel
import retrofit2.http.GET

internal interface GenreApi {

  @GET("genre/movie/list")
  suspend fun getGenres(): Result<GenreListModel>
}
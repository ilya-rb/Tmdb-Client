package com.tmdbclient.servicetmdb.api

import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.model.GenreListModel
import retrofit2.http.GET

interface GenreApi {

  @GET("genre/movie/list")
  suspend fun getGenres(): Result<GenreListModel>
}
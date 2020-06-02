package com.illiarb.tmdbclient.services.tmdb.internal.network.api

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.internal.model.GenreListModel
import retrofit2.http.GET

internal interface GenreApi {

  @GET("genre/movie/list")
  suspend fun getGenres(): Result<GenreListModel>
}
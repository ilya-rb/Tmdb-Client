package com.illiarb.tmdbclient.services.tmdb.api.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.api.domain.Genre

interface GenresInteractor {

  suspend fun getAllGenres(): Result<List<Genre>>
}
package com.tmdbclient.servicetmdb.interactor

import com.illiarb.tmdbclient.util.Result
import com.tmdbclient.servicetmdb.domain.Genre

interface GenresInteractor {

  suspend fun getAllGenres(): Result<List<Genre>>
}
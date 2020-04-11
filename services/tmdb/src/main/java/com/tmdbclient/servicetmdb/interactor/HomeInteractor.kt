package com.tmdbclient.servicetmdb.interactor

import com.illiarb.tmdbclient.util.Result
import com.tmdbclient.servicetmdb.domain.MovieSection

interface HomeInteractor {

  companion object {
    // Max genres displayed in the section
    const val GENRES_MAX_SIZE = 8
  }

  suspend fun getHomeSections(): Result<List<MovieSection>>
}
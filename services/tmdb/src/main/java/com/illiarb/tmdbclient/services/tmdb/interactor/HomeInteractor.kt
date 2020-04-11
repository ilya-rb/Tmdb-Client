package com.illiarb.tmdbclient.services.tmdb.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.MovieSection

interface HomeInteractor {

  companion object {
    // Max genres displayed in the section
    const val GENRES_MAX_SIZE = 8
  }

  suspend fun getHomeSections(): Result<List<MovieSection>>
}
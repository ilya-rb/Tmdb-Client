package com.illiarb.tmdbclient.libs.test.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.PagedList
import com.illiarb.tmdbclient.services.tmdb.interactor.SearchInteractor

class TestSearchInteractor : SearchInteractor {

  override suspend fun searchMovies(query: String): Result<PagedList<Movie>> {
    return Result.create {
      PagedList(emptyList<Movie>(), 1, 1)
    }
  }
}
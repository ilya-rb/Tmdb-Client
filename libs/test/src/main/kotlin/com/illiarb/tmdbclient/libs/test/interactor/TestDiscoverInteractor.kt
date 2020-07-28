package com.illiarb.tmdbclient.libs.test.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Filter
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.PagedList
import com.illiarb.tmdbclient.services.tmdb.interactor.DiscoverInteractor

class TestDiscoverInteractor : DiscoverInteractor {

  override suspend fun discoverMovies(filter: Filter, page: Int): Result<PagedList<Movie>> {
    return Result.create {
      PagedList(emptyList<Movie>(), 1, 1)
    }
  }
}
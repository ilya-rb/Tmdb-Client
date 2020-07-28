package com.illiarb.tmdbclient.services.tmdb.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Filter
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.PagedList

interface DiscoverInteractor {

  suspend fun discoverMovies(filter: Filter, page: Int): Result<PagedList<Movie>>
}
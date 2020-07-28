package com.illiarb.tmdbclient.services.tmdb.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.PagedList

interface SearchInteractor {

  suspend fun searchMovies(query: String): Result<PagedList<Movie>>
}
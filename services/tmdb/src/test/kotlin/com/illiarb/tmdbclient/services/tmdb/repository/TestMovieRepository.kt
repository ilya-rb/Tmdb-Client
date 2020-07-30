package com.illiarb.tmdbclient.services.tmdb.repository

import com.illiarb.tmdbclient.libs.test.entity.FakeEntityFactory
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.MovieFilter
import com.illiarb.tmdbclient.services.tmdb.internal.repository.MoviesRepository

class TestMovieRepository : MoviesRepository {

  override suspend fun getMoviesByType(type: String, refresh: Boolean): Result<List<Movie>> {
    return FakeEntityFactory.getMoviesByType(type, refresh)
  }

  override suspend fun getMovieDetails(id: Int, appendToResponse: String): Result<Movie> {
    return FakeEntityFactory.getMovieDetails(id, appendToResponse)
  }

  override suspend fun getMovieFilters(): Result<List<MovieFilter>> {
    return Result.Ok(FakeEntityFactory.movieFilters)
  }
}
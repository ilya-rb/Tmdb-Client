package com.tmdbclient.servicetmdb.repository

import com.illiarb.tmdbclient.util.Result
import com.illiarb.tmdbcliient.coretest.entity.FakeEntityFactory
import com.tmdbclient.servicetmdb.domain.Movie
import com.tmdbclient.servicetmdb.domain.MovieFilter
import com.tmdbclient.servicetmdb.domain.Review
import com.tmdbclient.servicetmdb.internal.repository.MoviesRepository

class TestMovieRepository : MoviesRepository {

  override suspend fun getMoviesByType(type: String, refresh: Boolean): Result<List<Movie>> {
    return FakeEntityFactory.getMoviesByType(type, refresh)
  }

  override suspend fun getMovieDetails(id: Int, appendToResponse: String): Result<Movie> {
    return FakeEntityFactory.getMovieDetails(id, appendToResponse)
  }

  override suspend fun getMovieReviews(id: Int): Result<List<Review>> {
    return Result.Ok(emptyList())
  }

  override suspend fun getMovieFilters(): Result<List<MovieFilter>> {
    return Result.Ok(FakeEntityFactory.movieFilters)
  }
}
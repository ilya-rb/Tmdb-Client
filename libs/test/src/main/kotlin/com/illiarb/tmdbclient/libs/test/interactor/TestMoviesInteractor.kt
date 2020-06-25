package com.illiarb.tmdbclient.libs.test.interactor

import com.illiarb.tmdbclient.libs.test.entity.FakeEntityFactory
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.api.domain.Filter
import com.illiarb.tmdbclient.services.tmdb.api.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.api.domain.MovieBlock
import com.illiarb.tmdbclient.services.tmdb.api.domain.PagedList
import com.illiarb.tmdbclient.services.tmdb.api.domain.Video
import com.illiarb.tmdbclient.services.tmdb.api.interactor.MoviesInteractor

class TestMoviesInteractor : MoviesInteractor {

  override suspend fun getAllMovies(): Result<List<MovieBlock>> {
    val filters = FakeEntityFactory.movieFilters
    val result = mutableListOf<MovieBlock>()

    filters.forEach {
      val movies = FakeEntityFactory.getMoviesByType(it.code, false).unwrap()
      result.add(MovieBlock(it, movies))
    }

    return Result.Ok(result)
  }

  override suspend fun getMovieDetails(movieId: Int): Result<Movie> {
    return FakeEntityFactory.getMovieDetails(movieId, "")
  }

  override suspend fun getSimilarMovies(movieId: Int): Result<List<Movie>> {
    return Result.Ok(emptyList())
  }

  override suspend fun getMovieVideos(movieId: Int): Result<List<Video>> {
    return Result.Ok(emptyList())
  }

  @Suppress("MagicNumber")
  override suspend fun discoverMovies(filter: Filter, page: Int): Result<PagedList<Movie>> {
    val movieList = FakeEntityFactory.createFakeMovieList(5) {
      FakeEntityFactory.createFakeMovie().copy(
        genres = filter.selectedGenreIds.map { FakeEntityFactory.createGenre(it) }
      )
    }
    return Result.Ok(PagedList(movieList, page = 1, totalPages = 1))
  }

  override suspend fun searchMovies(query: String): Result<PagedList<Movie>> {
    return Result.Ok(PagedList(emptyList(), 1, 1))
  }
}
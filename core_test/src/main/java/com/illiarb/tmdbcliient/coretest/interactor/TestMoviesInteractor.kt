package com.illiarb.tmdbcliient.coretest.interactor

import com.illiarb.tmdbclient.util.Result
import com.illiarb.tmdbcliient.coretest.entity.FakeEntityFactory
import com.tmdbclient.servicetmdb.domain.Movie
import com.tmdbclient.servicetmdb.domain.MovieBlock
import com.tmdbclient.servicetmdb.domain.Video
import com.tmdbclient.servicetmdb.interactor.MoviesInteractor

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
  override suspend fun discoverMovies(genreId: Int): Result<List<Movie>> {
    val movieList = FakeEntityFactory.createFakeMovieList(5) {
      FakeEntityFactory.createFakeMovie().copy(
        genres = listOf(FakeEntityFactory.createGenre(genreId))
      )
    }
    return Result.Ok(movieList)
  }
}
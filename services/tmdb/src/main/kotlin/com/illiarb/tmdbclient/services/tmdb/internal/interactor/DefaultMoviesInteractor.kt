package com.illiarb.tmdbclient.services.tmdb.internal.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.MovieBlock
import com.illiarb.tmdbclient.services.tmdb.domain.MovieFilter
import com.illiarb.tmdbclient.services.tmdb.domain.Video
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.MovieMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.MovieApi
import com.illiarb.tmdbclient.services.tmdb.internal.repository.ConfigurationRepository
import com.illiarb.tmdbclient.services.tmdb.internal.repository.MoviesRepository
import javax.inject.Inject

internal class DefaultMoviesInteractor @Inject constructor(
  private val repository: MoviesRepository,
  private val movieApi: MovieApi,
  private val movieMapper: MovieMapper,
  private val configurationRepository: ConfigurationRepository
) : MoviesInteractor {

  override suspend fun getAllMovies(): Result<List<MovieBlock>> {
    return repository.getMovieFilters().mapOnSuccess { filters ->
      filters.map { filter ->
        val moviesByType = when (val result = getMoviesByType(filter)) {
          is Result.Ok -> result.data
          is Result.Err -> emptyList()
        }
        MovieBlock(filter, moviesByType)
      }
    }
  }

  override suspend fun getMovieDetails(movieId: Int): Result<Movie> {
    val configuration = when (val result = configurationRepository.getConfiguration()) {
      is Result.Ok -> result.data
      is Result.Err -> null
    }
    val imageKey = configuration?.changeKeys?.find { it == MoviesInteractor.KEY_INCLUDE_IMAGES }
    val videoKey = configuration?.changeKeys?.find { it == MoviesInteractor.KEY_INCLUDE_VIDEOS }
    val keys = buildString {
      imageKey?.let { append(it) }
      videoKey?.let {
        if (isNotEmpty()) {
          append(",")
        }
        append(it)
      }
    }
    return repository.getMovieDetails(movieId, keys)
  }

  override suspend fun getSimilarMovies(movieId: Int): Result<List<Movie>> {
    val config = configurationRepository.getConfiguration()
    if (config.isError()) {
      return Result.Err(config.error())
    }

    return movieApi.getSimilarMovies(movieId).mapOnSuccess {
      movieMapper.mapList(config.unwrap(), it.results)
    }
  }

  override suspend fun getMovieVideos(movieId: Int): Result<List<Video>> {
    return movieApi.getMovieVideos(movieId).mapOnSuccess { it.results }
  }

  private suspend fun getMoviesByType(filter: MovieFilter): Result<List<Movie>> =
    repository.getMoviesByType(filter.code)
}
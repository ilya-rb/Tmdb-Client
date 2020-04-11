package com.tmdbclient.servicetmdb.internal.interactor

import com.illiarb.tmdbclient.tools.DispatcherProvider
import com.tmdbclient.servicetmdb.domain.Genre
import com.tmdbclient.servicetmdb.domain.Movie
import com.tmdbclient.servicetmdb.domain.MovieBlock
import com.tmdbclient.servicetmdb.domain.MovieFilter
import com.tmdbclient.servicetmdb.domain.Video
import com.illiarb.tmdbclient.util.Result
import com.tmdbclient.servicetmdb.interactor.MoviesInteractor
import com.tmdbclient.servicetmdb.internal.network.api.DiscoverApi
import com.tmdbclient.servicetmdb.internal.network.api.MovieApi
import com.tmdbclient.servicetmdb.internal.cache.TmdbCache
import com.tmdbclient.servicetmdb.internal.network.mappers.MovieMapper
import com.tmdbclient.servicetmdb.internal.repository.MoviesRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultMoviesInteractor @Inject constructor(
  private val repository: MoviesRepository,
  private val discoverApi: DiscoverApi,
  private val movieApi: MovieApi,
  private val movieMapper: MovieMapper,
  private val cache: TmdbCache,
  private val dispatcherProvider: DispatcherProvider
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
    val configuration = withContext(dispatcherProvider.io) { cache.getConfiguration() }
    val imageKey = configuration.changeKeys.find { it == MoviesInteractor.KEY_INCLUDE_IMAGES }
    val videoKey = configuration.changeKeys.find { it == MoviesInteractor.KEY_INCLUDE_VIDEOS }
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

  override suspend fun discoverMovies(genreId: Int): Result<List<Movie>> {
    val id = if (genreId == Genre.GENRE_ALL) null else genreId.toString()
    val config = withContext(dispatcherProvider.io) {
      cache.getConfiguration()
    }

    return discoverApi.discoverMovies(id).mapOnSuccess {
      movieMapper.mapList(config, it.results)
    }
  }

  override suspend fun getSimilarMovies(movieId: Int): Result<List<Movie>> {
    val config = withContext(dispatcherProvider.io) {
      cache.getConfiguration()
    }

    return movieApi.getSimilarMovies(movieId).mapOnSuccess {
      movieMapper.mapList(config, it.results)
    }
  }

  override suspend fun getMovieVideos(movieId: Int): Result<List<Video>> {
    return movieApi.getMovieVideos(movieId).mapOnSuccess { it.results }
  }

  private suspend fun getMoviesByType(filter: MovieFilter): Result<List<Movie>> =
    repository.getMoviesByType(filter.code)
}
package com.illiarb.tmdbclient.services.tmdb.internal.repository

import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.R
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.MovieFilter
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.dto.MovieDto
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.MovieMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.MovieApi
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal interface MoviesRepository {

  suspend fun getMoviesByType(type: String, refresh: Boolean = false): Result<List<Movie>>

  suspend fun getMovieDetails(id: Int, appendToResponse: String): Result<Movie>

  suspend fun getMovieFilters(): Result<List<MovieFilter>>

}

internal class DefaultMoviesRepository @Inject constructor(
  private val moviesService: MovieApi,
  private val dispatcherProvider: DispatcherProvider,
  private val configurationRepository: ConfigurationRepository,
  private val cache: TmdbCache,
  private val movieMapper: MovieMapper,
  private val resourceResolver: ResourceResolver
) : MoviesRepository {

  override suspend fun getMoviesByType(type: String, refresh: Boolean): Result<List<Movie>> =
    Result.create {
      withContext(dispatcherProvider.io) {
        val configuration = configurationRepository.getConfiguration(refresh = false).unwrap()

        if (refresh) {
          return@withContext movieMapper.mapList(configuration, fetchFromNetworkAndStore(type))
        }

        val cached = cache.getMoviesByType(type)
        if (cached.isExpired() || cached.movies.isEmpty()) {
          movieMapper.mapList(configuration, fetchFromNetworkAndStore(type))
        } else {
          movieMapper.mapList(configuration, cached.movies)
        }
      }
    }

  override suspend fun getMovieDetails(id: Int, appendToResponse: String): Result<Movie> =
    Result.create {
      withContext(dispatcherProvider.io) {
        val details = moviesService.getMovieDetails(id, appendToResponse).unwrap()
        val configuration = configurationRepository.getConfiguration(refresh = false).unwrap()
        movieMapper.map(configuration, details)
      }
    }

  override suspend fun getMovieFilters(): Result<List<MovieFilter>> = Result.create {
    withContext(dispatcherProvider.io) {
      resourceResolver.getStringArray(R.array.movie_filters_codes)
        .zip(resourceResolver.getStringArray(R.array.movie_filters)) { filter, code ->
          MovieFilter(code, filter)
        }
    }
  }

  private suspend fun fetchFromNetworkAndStore(type: String): List<MovieDto> {
    val result = moviesService.getMoviesByType(type).unwrap().results
    cache.storeMovies(type, result)
    return result
  }
}
package com.illiarb.tmdbclient.services.tmdb.internal.repository

import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.MovieFilter
import com.illiarb.tmdbclient.services.tmdb.domain.Review
import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.libs.tools.ResourceResolver
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.R
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.MovieApi
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.MovieMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.ReviewMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.MovieModel
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

internal interface MoviesRepository {

  suspend fun getMoviesByType(type: String, refresh: Boolean = false): Result<List<Movie>>

  suspend fun getMovieDetails(id: Int, appendToResponse: String): Result<Movie>

  suspend fun getMovieReviews(id: Int): Result<List<Review>>

  suspend fun getMovieFilters(): Result<List<MovieFilter>>

}

@Singleton
internal class DefaultMoviesRepository @Inject constructor(
  private val moviesService: MovieApi,
  private val dispatcherProvider: DispatcherProvider,
  private val persistableStorage: TmdbCache,
  private val movieMapper: MovieMapper,
  private val reviewMapper: ReviewMapper,
  private val resourceResolver: ResourceResolver
) : MoviesRepository {

  override suspend fun getMoviesByType(type: String, refresh: Boolean): Result<List<Movie>> =
    Result.create {
      withContext(dispatcherProvider.io) {
        val configuration = persistableStorage.getConfiguration()

        if (refresh) {
          return@withContext movieMapper.mapList(configuration, fetchFromNetworkAndStore(type))
        }

        val cached = persistableStorage.getMoviesByType(type)
        if (cached.isEmpty()) {
          movieMapper.mapList(configuration, fetchFromNetworkAndStore(type))
        } else {
          movieMapper.mapList(configuration, cached)
        }
      }
    }

  override suspend fun getMovieDetails(id: Int, appendToResponse: String): Result<Movie> =
    Result.create {
      withContext(dispatcherProvider.io) {
        val details = moviesService.getMovieDetails(id, appendToResponse).unwrap()
        val configuration = persistableStorage.getConfiguration()
        movieMapper.map(configuration, details)
      }
    }

  override suspend fun getMovieReviews(id: Int): Result<List<Review>> =
    Result.create {
      withContext(dispatcherProvider.io) {
        val reviews = moviesService.getMovieReviews(id).unwrap()
        reviewMapper.mapList(reviews.results)
      }
    }

  override suspend fun getMovieFilters(): Result<List<MovieFilter>> = Result.create {
    withContext(dispatcherProvider.io) {
      resourceResolver
        .getStringArray(R.array.movie_filters)
        .map { MovieFilter(it, it.toLowerCase(Locale.getDefault()).replace(" ", "_")) }
    }
  }

  private suspend fun fetchFromNetworkAndStore(type: String): List<MovieModel> {
    val result = moviesService.getMoviesByType(type).unwrap().results
    persistableStorage.storeMovies(type, result)
    return result
  }
}
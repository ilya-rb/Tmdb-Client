package com.tmdbclient.servicetmdb.repository

import com.illiarb.tmdbclient.storage.R
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieFilter
import com.illiarb.tmdblcient.core.domain.Review
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.api.MovieApi
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.mappers.MovieMapper
import com.tmdbclient.servicetmdb.mappers.ReviewMapper
import com.tmdbclient.servicetmdb.model.MovieModel
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

interface MoviesRepository {

  suspend fun getMoviesByType(type: String, refresh: Boolean = false): Result<List<Movie>>

  suspend fun getMovieDetails(id: Int, appendToResponse: String): Result<Movie>

  suspend fun getMovieReviews(id: Int): Result<List<Review>>

  suspend fun getMovieFilters(): Result<List<MovieFilter>>

}

@Singleton
class DefaultMoviesRepository @Inject constructor(
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
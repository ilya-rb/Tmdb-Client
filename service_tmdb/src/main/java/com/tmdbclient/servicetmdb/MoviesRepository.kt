package com.tmdbclient.servicetmdb

import com.illiarb.tmdbclient.storage.R
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieFilter
import com.illiarb.tmdblcient.core.domain.Review
import com.illiarb.tmdblcient.core.storage.ResourceResolver
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import com.tmdbclient.servicetmdb.api.MovieApi
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.mappers.MovieMapper
import com.tmdbclient.servicetmdb.mappers.ReviewMapper
import com.tmdbclient.servicetmdb.model.MovieModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

interface MoviesRepository {

    suspend fun getMoviesByType(type: String, refresh: Boolean = false): List<Movie>

    suspend fun getMovieDetails(id: Int, appendToResponse: String): Movie

    suspend fun getMovieReviews(id: Int): List<Review>

    suspend fun getMovieFilters(): List<MovieFilter>

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

    override suspend fun getMoviesByType(type: String, refresh: Boolean): List<Movie> =
        withContext(dispatcherProvider.io) {
            if (refresh) {
                return@withContext movieMapper.mapList(fetchFromNetworkAndStore(type))
            }

            val cached = persistableStorage.getMoviesByType(type)
            if (cached.isEmpty()) {
                movieMapper.mapList(fetchFromNetworkAndStore(type))
            } else {
                movieMapper.mapList(cached)
            }
        }

    override suspend fun getMovieDetails(id: Int, appendToResponse: String): Movie =
        withContext(dispatcherProvider.io) {
            val details = moviesService.getMovieDetailsAsync(id, appendToResponse).await()
            movieMapper.map(details)
        }

    override suspend fun getMovieReviews(id: Int): List<Review> =
        withContext(dispatcherProvider.io) {
            val reviews = moviesService.getMovieReviewsAsync(id).await()
            reviewMapper.mapList(reviews.results)
        }

    override suspend fun getMovieFilters(): List<MovieFilter> =
        withContext(dispatcherProvider.io) {
            resourceResolver
                .getStringArray(R.array.movie_filters)
                .map {
                    MovieFilter(
                        it,
                        it.toLowerCase(Locale.getDefault()).replace(" ", "_")
                    )
                }
        }

    private suspend fun fetchFromNetworkAndStore(type: String): List<MovieModel> =
        coroutineScope {
            val result = moviesService.getMoviesByTypeAsync(type).await().results
            persistableStorage.storeMovies(type, result)
            result
        }
}
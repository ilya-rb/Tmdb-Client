package com.illiarb.tmdbclient.storage.repositories

import com.illiarb.tmdbclient.storage.R
import com.illiarb.tmdbclient.storage.local.PersistableStorage
import com.illiarb.tmdbclient.storage.mappers.MovieMapper
import com.illiarb.tmdbclient.storage.mappers.ReviewMapper
import com.illiarb.tmdbclient.storage.model.MovieModel
import com.illiarb.tmdbclient.storage.network.api.service.MovieService
import com.illiarb.tmdbclient.storage.network.api.service.SearchService
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import com.illiarb.tmdblcient.core.system.ResourceResolver
import com.illiarb.tmdblcient.core.system.coroutine.DispatcherProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author ilya-rb on 25.10.18.
 */
@Singleton
class MoviesRepositoryImpl @Inject constructor(
    private val moviesService: MovieService,
    private val searchService: SearchService,
    private val dispatcherProvider: DispatcherProvider,
    private val persistableStorage: PersistableStorage,
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
            val details = moviesService.getMovieDetails(id, appendToResponse).await()
            movieMapper.map(details)
        }

    override suspend fun getMovieReviews(id: Int): List<Review> =
        withContext(dispatcherProvider.io) {
            val reviews = moviesService.getMovieReviews(id).await()
            reviewMapper.mapList(reviews.results)
        }

    override suspend fun getMovieFilters(): List<MovieFilter> =
        withContext(dispatcherProvider.io) {
            resourceResolver
                .getStringArray(R.array.movie_filters)
                .map { MovieFilter(it, it.toLowerCase().replace(" ", "_")) }
        }

    override suspend fun searchMovies(query: String): List<Movie> =
        withContext(dispatcherProvider.io) {
            val movies = searchService.searchMovies(query).await()
            movieMapper.mapList(movies.results)
        }

    private suspend fun fetchFromNetworkAndStore(type: String): List<MovieModel> = coroutineScope {
        val result = moviesService.getMoviesByType(type).await().results
        persistableStorage.storeMovies(type, result)
        result
    }
}
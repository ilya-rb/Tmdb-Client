package com.tmdbclient.servicetmdb.interactor

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.MovieFilter
import com.illiarb.tmdblcient.core.domain.Video
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.api.DiscoverApi
import com.tmdbclient.servicetmdb.api.MovieApi
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.mappers.MovieMapper
import com.tmdbclient.servicetmdb.repository.MoviesRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultMoviesInteractor @Inject constructor(
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
                    is Result.Success -> result.data
                    is Result.Error -> emptyList()
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
        return Result.create {
            val id = if (genreId == Genre.GENRE_ALL) null else genreId.toString()
            val movies = discoverApi.discoverMoviesAsync(id).await().results
            movieMapper.mapList(movies)
        }
    }

    override suspend fun getSimilarMovies(movieId: Int): Result<List<Movie>> {
        return Result.create {
            val results = movieApi.getSimilarMoviesAsync(movieId).await()
            movieMapper.mapList(results.results)
        }
    }

    override suspend fun getMovieVideos(movieId: Int): Result<List<Video>> {
        return Result.create {
            movieApi.getMovieVideosAsync(movieId).await().results
        }
    }

    private suspend fun getMoviesByType(filter: MovieFilter): Result<List<Movie>> =
        repository.getMoviesByType(filter.code)
}
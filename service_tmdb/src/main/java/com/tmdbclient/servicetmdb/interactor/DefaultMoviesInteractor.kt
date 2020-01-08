package com.tmdbclient.servicetmdb.interactor

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.MovieFilter
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.repository.MoviesRepository
import com.tmdbclient.servicetmdb.api.DiscoverApi
import com.tmdbclient.servicetmdb.mappers.MovieMapper
import java.util.Collections
import javax.inject.Inject

class DefaultMoviesInteractor @Inject constructor(
    private val repository: MoviesRepository,
    private val discoverApi: DiscoverApi,
    private val movieMapper: MovieMapper
) : MoviesInteractor {

    override suspend fun getAllMovies(): Result<List<MovieBlock>> {
        return repository.getMovieFilters().mapOnSuccess { filters ->
            filters.map {
                val moviesByType = when (val result = getMoviesByType(it)) {
                    is Result.Success -> result.data
                    is Result.Error -> Collections.emptyList()
                }
                MovieBlock(it, moviesByType)
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie> =
        repository.getMovieDetails(movieId, "images,reviews")

    override suspend fun discoverMovies(genreId: Int): Result<List<Movie>> {
        return Result.create {
            val id = if (genreId == Genre.GENRE_ALL) null else genreId
            val movies = discoverApi.discoverMoviesAsync(id.toString()).await().results
            movieMapper.mapList(movies)
        }
    }

    private suspend fun getMoviesByType(filter: MovieFilter): Result<List<Movie>> =
        repository.getMoviesByType(filter.code, false)
}
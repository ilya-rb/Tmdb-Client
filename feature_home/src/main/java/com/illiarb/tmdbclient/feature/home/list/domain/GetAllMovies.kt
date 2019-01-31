package com.illiarb.tmdbclient.feature.home.list.domain

import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.NonBlockingUseCase
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.domain.entity.MovieFilter
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.illiarb.tmdblcient.core.storage.MoviesRepository
import javax.inject.Inject

/**
 * @author ilya-rb on 09.01.19.
 */
class GetAllMovies @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val errorHandler: ErrorHandler
) : NonBlockingUseCase<List<MovieBlock>, Unit> {

    override suspend fun executeAsync(payload: Unit): Result<List<MovieBlock>> =
        Result.create(errorHandler) {
            val movieFilters = moviesRepository.getMovieFilters()

            movieFilters.map { filter ->
                MovieBlock(filter, getMoviesByType(filter))
            }
        }

    private suspend fun getMoviesByType(filter: MovieFilter): List<Movie> =
        moviesRepository.getMoviesByType(filter.code, true)
}

data class MovieBlock(val filter: MovieFilter, val movies: List<Movie>)
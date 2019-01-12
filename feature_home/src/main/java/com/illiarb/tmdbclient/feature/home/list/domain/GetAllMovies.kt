package com.illiarb.tmdbclient.feature.home.list.domain

import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.common.invokeForResult
import com.illiarb.tmdblcient.core.domain.NonBlockingUseCase
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import javax.inject.Inject

/**
 * @author ilya-rb on 09.01.19.
 */
class GetAllMovies @Inject constructor(
    private val moviesRepository: MoviesRepository
) : NonBlockingUseCase<Result<List<Pair<MovieFilter, List<Movie>>>>, Unit> {

    override suspend fun executeAsync(payload: Unit): Result<List<Pair<MovieFilter, List<Movie>>>> =
        invokeForResult {
            moviesRepository.getMovieFilters().map { filter ->
                Pair(filter, getMoviesByType(filter))
            }
        }

    private suspend fun getMoviesByType(filter: MovieFilter): List<Movie> =
        when (val result = moviesRepository.getMoviesByType(filter.code, true)) {
            is Result.Success -> result.result
            is Result.Error -> throw result.error
        }
}
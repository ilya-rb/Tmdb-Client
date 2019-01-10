package com.illiarb.tmdbclient.feature.home.domain

import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.common.invokeForResult
import com.illiarb.tmdblcient.core.domain.UseCase
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author ilya-rb on 09.01.19.
 */
class GetAllMovies @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val dispatcherProvider: DispatcherProvider
) : UseCase<Result<List<Pair<MovieFilter, List<Movie>>>>, Unit> {

    override suspend fun execute(payload: Unit): Result<List<Pair<MovieFilter, List<Movie>>>> = invokeForResult {
        withContext(dispatcherProvider.ioDispatcher) {
            moviesRepository.getMovieFilters().map { filter ->
                Pair(filter, getMoviesByType(filter))
            }
        }
    }

    private suspend fun getMoviesByType(filter: MovieFilter): List<Movie> =
        when (val result = moviesRepository.getMoviesByType(filter.code, true)) {
            is Result.Success -> result.result
            is Result.Error -> throw result.error
        }
}
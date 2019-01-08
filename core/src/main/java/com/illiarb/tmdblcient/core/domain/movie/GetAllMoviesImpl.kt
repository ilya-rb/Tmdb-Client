package com.illiarb.tmdblcient.core.domain.movie

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class GetAllMoviesImpl @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val getMovieFilters: GetMovieFilters
) : GetAllMovies {

    override suspend fun invoke(): List<Pair<MovieFilter, List<Movie>>> = withContext(dispatcherProvider.ioDispatcher) {
        getMovieFilters().map { filter ->
            Pair(filter, moviesRepository.getMoviesByType(filter.code, refresh = true))
        }
    }
}
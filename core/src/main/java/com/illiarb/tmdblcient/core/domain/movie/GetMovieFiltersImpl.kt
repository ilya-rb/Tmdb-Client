package com.illiarb.tmdblcient.core.domain.movie

import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import com.illiarb.tmdblcient.core.system.NonBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class GetMovieFiltersImpl @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val dispatcherProvider: DispatcherProvider
) : GetMovieFilters {

    @NonBlocking
    override suspend fun invoke(): List<MovieFilter> = withContext(dispatcherProvider.ioDispatcher) {
        moviesRepository.getMovieFilters()
    }
}
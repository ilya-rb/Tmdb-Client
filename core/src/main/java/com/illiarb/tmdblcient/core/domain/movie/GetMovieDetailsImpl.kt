package com.illiarb.tmdblcient.core.domain.movie

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import com.illiarb.tmdblcient.core.system.NonBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class GetMovieDetailsImpl @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val dispatcherProvider: DispatcherProvider
) : GetMovieDetails {

    @NonBlocking
    override suspend fun invoke(id: Int): Movie = withContext(dispatcherProvider.ioDispatcher) {
        moviesRepository.getMovieDetails(id, "images,reviews")
    }
}
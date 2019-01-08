package com.illiarb.tmdblcient.core.domain.movie

import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import com.illiarb.tmdblcient.core.system.DispatcherProvider
import com.illiarb.tmdblcient.core.system.NonBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class GetMovieReviewsImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val moviesRepository: MoviesRepository
) : GetMovieReviews {

    @NonBlocking
    override suspend fun invoke(movieId: Int): List<Review> = withContext(dispatcherProvider.ioDispatcher) {
        moviesRepository.getMovieReviews(movieId)
    }
}
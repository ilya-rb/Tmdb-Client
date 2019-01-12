package com.illiarb.tmdbclient.feature.home.details.domain

import com.illiarb.tmdblcient.core.domain.NonBlockingUseCase
import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import com.illiarb.tmdblcient.core.system.coroutine.NonBlocking
import javax.inject.Inject

/**
 * @author ilya-rb on 09.01.19.
 */
class GetMovieReviews @Inject constructor(
    private val moviesRepository: MoviesRepository
) : NonBlockingUseCase<List<Review>, Int> {

    @NonBlocking
    override suspend fun executeAsync(payload: Int): List<Review> {
        return moviesRepository.getMovieReviews(payload)
    }
}
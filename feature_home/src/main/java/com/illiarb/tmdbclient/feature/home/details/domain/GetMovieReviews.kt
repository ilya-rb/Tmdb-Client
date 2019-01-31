package com.illiarb.tmdbclient.feature.home.details.domain

import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.NonBlockingUseCase
import com.illiarb.tmdblcient.core.domain.entity.Review
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.illiarb.tmdblcient.core.storage.MoviesRepository
import com.illiarb.tmdblcient.core.common.NonBlocking
import javax.inject.Inject

/**
 * @author ilya-rb on 09.01.19.
 */
class GetMovieReviews @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val errorHandler: ErrorHandler
) : NonBlockingUseCase<List<Review>, Int> {

    @NonBlocking
    override suspend fun executeAsync(payload: Int): Result<List<Review>> {
        return Result.create(errorHandler) {
            moviesRepository.getMovieReviews(payload)
        }
    }
}
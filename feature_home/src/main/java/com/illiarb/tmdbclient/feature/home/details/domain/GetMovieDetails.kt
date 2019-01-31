package com.illiarb.tmdbclient.feature.home.details.domain

import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.NonBlockingUseCase
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.illiarb.tmdblcient.core.storage.MoviesRepository
import com.illiarb.tmdblcient.core.common.NonBlocking
import javax.inject.Inject

/**
 * @author ilya-rb on 09.01.19.
 */
class GetMovieDetails @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val errorHandler: ErrorHandler
) : NonBlockingUseCase<Movie, Int> {

    @NonBlocking
    override suspend fun executeAsync(payload: Int): Result<Movie> {
        return Result.create(errorHandler) {
            moviesRepository.getMovieDetails(payload, "images,reviews")
        }
    }
}
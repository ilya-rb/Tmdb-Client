package com.illiarb.tmdbclient.feature.home.domain

import com.illiarb.tmdblcient.core.domain.UseCase
import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import com.illiarb.tmdblcient.core.system.NonBlocking
import javax.inject.Inject

/**
 * @author ilya-rb on 09.01.19.
 */
class GetMovieReviews @Inject constructor(
    private val moviesRepository: MoviesRepository
) : UseCase<List<Review>, Int> {

    @NonBlocking
    override suspend fun execute(payload: Int): List<Review> {
        return moviesRepository.getMovieReviews(payload)
    }
}
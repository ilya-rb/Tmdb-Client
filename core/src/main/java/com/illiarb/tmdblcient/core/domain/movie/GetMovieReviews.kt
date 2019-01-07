package com.illiarb.tmdblcient.core.domain.movie

import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.system.NonBlocking

/**
 * @author ilya-rb on 07.01.19.
 */
interface GetMovieReviews {

    @NonBlocking
    suspend operator fun invoke(movieId: Int): List<Review>
}
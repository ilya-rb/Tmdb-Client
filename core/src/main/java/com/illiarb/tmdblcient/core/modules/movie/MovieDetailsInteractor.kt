package com.illiarb.tmdblcient.core.modules.movie

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.Review
import io.reactivex.Single

/**
 * @author ilya-rb on 18.11.18.
 */
interface MovieDetailsInteractor {

    fun getMovieDetails(id: Int, appendToResponse: String): Single<Movie>

    fun getMovieReviews(id: Int): Single<List<Review>>

}
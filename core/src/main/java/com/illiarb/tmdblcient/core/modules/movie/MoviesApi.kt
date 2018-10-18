package com.illiarb.tmdblcient.core.modules.movie

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.Review
import io.reactivex.Single

interface MoviesApi {

    fun getMoviesByType(type: String): Single<List<Movie>>

    fun getMovieDetails(id: Int, appendToResponse: String): Single<Movie>

    fun getMovieReviews(id: Int): Single<List<Review>>
}
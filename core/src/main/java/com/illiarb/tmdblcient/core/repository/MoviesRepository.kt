package com.illiarb.tmdblcient.core.repository

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.entity.Review
import io.reactivex.Single

/**
 * @author ilya-rb on 25.10.18.
 */
interface MoviesRepository {

    fun getMoviesByType(type: String): Single<List<Movie>>

    fun getMovieDetails(id: Int, appendToResponse: String): Single<Movie>

    fun getMovieReviews(id: Int): Single<List<Review>>

    fun getMovieFilters(): Single<List<MovieFilter>>

    fun searchMovies(query: String): Single<List<Movie>>
}
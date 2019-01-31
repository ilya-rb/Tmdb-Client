package com.illiarb.tmdblcient.core.storage

import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.domain.entity.MovieFilter
import com.illiarb.tmdblcient.core.domain.entity.Review
import com.illiarb.tmdblcient.core.common.NonBlocking

/**
 * @author ilya-rb on 25.10.18.
 */
interface MoviesRepository {

    @NonBlocking
    suspend fun getMoviesByType(type: String, refresh: Boolean): List<Movie>

    @NonBlocking
    suspend fun getMovieDetails(id: Int, appendToResponse: String): Movie

    @NonBlocking
    suspend fun getMovieReviews(id: Int): List<Review>

    @NonBlocking
    suspend fun getMovieFilters(): List<MovieFilter>

    @NonBlocking
    suspend fun searchMovies(query: String): List<Movie>
}
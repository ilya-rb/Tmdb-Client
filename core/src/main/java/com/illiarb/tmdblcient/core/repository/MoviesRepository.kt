package com.illiarb.tmdblcient.core.repository

import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.system.coroutine.NonBlocking

/**
 * @author ilya-rb on 25.10.18.
 */
interface MoviesRepository {

    @NonBlocking
    suspend fun getMoviesByType(type: String, refresh: Boolean): Result<List<Movie>>

    @NonBlocking
    suspend fun getMovieDetails(id: Int, appendToResponse: String): Movie

    @NonBlocking
    suspend fun getMovieReviews(id: Int): List<Review>

    @NonBlocking
    suspend fun getMovieFilters(): List<MovieFilter>

    @NonBlocking
    suspend fun searchMovies(query: String): List<Movie>
}
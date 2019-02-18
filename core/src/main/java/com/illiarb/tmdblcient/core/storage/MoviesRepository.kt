package com.illiarb.tmdblcient.core.storage

import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.domain.entity.MovieFilter
import com.illiarb.tmdblcient.core.domain.entity.Review

/**
 * @author ilya-rb on 25.10.18.
 */
interface MoviesRepository {

    suspend fun getMoviesByType(type: String, refresh: Boolean): List<Movie>

    suspend fun getMovieDetails(id: Int, appendToResponse: String): Movie

    suspend fun getMovieReviews(id: Int): List<Review>

    suspend fun getMovieFilters(): List<MovieFilter>

    suspend fun searchMovies(query: String): List<Movie>
}
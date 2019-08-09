package com.illiarb.tmdblcient.core.domain

import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.domain.entity.MovieBlock
import com.illiarb.tmdblcient.core.domain.entity.Review

/**
 * @author ilya-rb on 18.02.19.
 */
interface MoviesInteractor {

    suspend fun getAllMovies(): List<MovieBlock>

    suspend fun getMovieDetails(id: Int): Movie

    suspend fun getMovieReviews(id: Int): List<Review>
}
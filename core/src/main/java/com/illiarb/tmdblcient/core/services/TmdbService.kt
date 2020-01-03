package com.illiarb.tmdblcient.core.services

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.Review
import com.illiarb.tmdblcient.core.domain.TrendingSection
import com.illiarb.tmdblcient.core.util.Result

interface TmdbService {

    suspend fun getAllMovies(): Result<List<MovieBlock>>

    suspend fun getTrending(): Result<TrendingSection>

    suspend fun getMovieGenres(): Result<List<Genre>>

    suspend fun getMovieDetails(id: Int): Result<Movie>

    suspend fun getMovieReviews(id: Int): Result<List<Review>>

    suspend fun discoverMovies(genreId: Int = -1): Result<List<Movie>>

}
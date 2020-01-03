package com.illiarb.tmdblcient.core.services

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.Review
import com.illiarb.tmdblcient.core.util.Result

interface TmdbService {

    suspend fun getMovieDetails(id: Int): Result<Movie>

    suspend fun getMovieReviews(id: Int): Result<List<Review>>

    suspend fun discoverMovies(genreId: Int = -1): Result<List<Movie>>

    suspend fun getMovieSections(): Result<List<MovieSection>>

    suspend fun getMovieGenres(): Result<List<Genre>>

}
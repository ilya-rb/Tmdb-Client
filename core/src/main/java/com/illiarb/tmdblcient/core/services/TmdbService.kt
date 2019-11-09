package com.illiarb.tmdblcient.core.services

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.Review
import com.illiarb.tmdblcient.core.util.Either

interface TmdbService {

    suspend fun getAllMovies(): Either<List<MovieBlock>>

    suspend fun getMovieGenres(): Either<List<Genre>>

    suspend fun getMovieDetails(id: Int): Either<Movie>

    suspend fun getMovieReviews(id: Int): Either<List<Review>>

}
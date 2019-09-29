package com.illiarb.tmdblcient.core.services

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.util.Async
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.Review
import com.illiarb.tmdblcient.core.util.Either
import kotlinx.coroutines.flow.Flow

interface TmdbService {

    fun getAllMovies(): Flow<Either<List<MovieBlock>>>

    fun getMovieGenres(): Flow<Either<List<Genre>>>

    fun getMovieDetails(id: Int): Flow<Either<Movie>>

    fun getMovieReviews(id: Int): Flow<Either<List<Review>>>

}
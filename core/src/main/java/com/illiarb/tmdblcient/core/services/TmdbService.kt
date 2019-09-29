package com.illiarb.tmdblcient.core.services

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.util.Async
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.Review
import kotlinx.coroutines.flow.Flow

interface TmdbService {

    fun getAllMovies(): Flow<List<MovieBlock>>

    fun getMovieGenres(): Flow<List<Genre>>

    fun getMovieDetails(id: Int): Flow<Async<Movie>>

    fun getMovieReviews(id: Int): Flow<Async<List<Review>>>

}
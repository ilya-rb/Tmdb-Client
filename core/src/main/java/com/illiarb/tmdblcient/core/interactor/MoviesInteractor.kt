package com.illiarb.tmdblcient.core.interactor

import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.util.Result

interface MoviesInteractor {

    suspend fun getAllMovies(): Result<List<MovieBlock>>

    suspend fun getMovieDetails(movieId: Int): Result<Movie>

    suspend fun discoverMovies(genreId: Int): Result<List<Movie>>
}
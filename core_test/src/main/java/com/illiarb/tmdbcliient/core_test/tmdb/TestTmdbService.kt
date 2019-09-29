package com.illiarb.tmdbcliient.core_test.tmdb

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.Review
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.util.Either
import com.tmdbclient.service_tmdb.MoviesRepository
import kotlinx.coroutines.flow.Flow

class TestTmdbService(
    private val moviesRepository: MoviesRepository = TestMovieRepository()
) : TmdbService {

    private val testGenres = listOf(
        Genre(0, "Action"),
        Genre(1, "Adventure"),
        Genre(2, "Animation"),
        Genre(3, "Comedy"),
        Genre(4, "Crime"),
        Genre(5, "Documentary")
    )

    override fun getAllMovies(): Flow<Either<List<MovieBlock>>> = Either.asFlow {
        moviesRepository.getMovieFilters().map {
            MovieBlock(it, moviesRepository.getMoviesByType(it.code))
        }
    }

    override fun getMovieGenres(): Flow<Either<List<Genre>>> = Either.asFlow { testGenres }

    override fun getMovieDetails(id: Int): Flow<Either<Movie>> = Either.asFlow {
        moviesRepository.getMovieDetails(id, "images,reviews")
    }

    override fun getMovieReviews(id: Int): Flow<Either<List<Review>>> = Either.asFlow {
        moviesRepository.getMovieReviews(id)
    }
}
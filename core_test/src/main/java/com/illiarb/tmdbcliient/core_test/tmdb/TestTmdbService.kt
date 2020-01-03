package com.illiarb.tmdbcliient.core_test.tmdb

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.Review
import com.illiarb.tmdblcient.core.domain.TrendingSection
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.service_tmdb.MoviesRepository
import java.util.Collections

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

    override suspend fun getAllMovies(): Result<List<MovieBlock>> = Result.create {
        moviesRepository.getMovieFilters().map {
            MovieBlock(it, moviesRepository.getMoviesByType(it.code))
        }
    }

    override suspend fun getTrending(): Result<TrendingSection> =
        Result.Success(TrendingSection(Collections.emptyList()))

    override suspend fun getMovieGenres(): Result<List<Genre>> = Result.create { testGenres }

    override suspend fun getMovieDetails(id: Int): Result<Movie> = Result.create {
        moviesRepository.getMovieDetails(id, "images,reviews")
    }

    override suspend fun getMovieReviews(id: Int): Result<List<Review>> = Result.create {
        moviesRepository.getMovieReviews(id)
    }

    override suspend fun discoverMovies(genreId: Int): Result<List<Movie>> =
        Result.create { Collections.emptyList<Movie>() }
}
package com.illiarb.tmdbcliient.coretest.interactor

import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.repository.MoviesRepository
import java.util.Collections

class TestMoviesInteractor(
    private val moviesRepository: MoviesRepository
) : MoviesInteractor {

    override suspend fun getAllMovies(): Result<List<MovieBlock>> {
        val filters = moviesRepository.getMovieFilters()
        val result = mutableListOf<MovieBlock>()

        filters.getOrThrow().forEach {
            val movies = moviesRepository.getMoviesByType(it.code).getOrThrow()
            result.add(MovieBlock(it, movies))
        }

        return Result.Success(result)
    }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return moviesRepository.getMovieDetails(movieId, "")
    }

    override suspend fun discoverMovies(genreId: Int): Result<List<Movie>> {
        return Result.Success(Collections.emptyList())
    }
}
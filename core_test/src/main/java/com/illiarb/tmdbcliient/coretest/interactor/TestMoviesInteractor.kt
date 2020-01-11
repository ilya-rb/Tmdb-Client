package com.illiarb.tmdbcliient.coretest.interactor

import com.illiarb.tmdbcliient.coretest.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.Video
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.repository.MoviesRepository

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

    override suspend fun getSimilarMovies(movieId: Int): Result<List<Movie>> {
        return Result.Success(emptyList())
    }

    override suspend fun getMovieVideos(movieId: Int): Result<List<Video>> {
        return Result.Success(emptyList())
    }

    @Suppress("MagicNumber")
    override suspend fun discoverMovies(genreId: Int): Result<List<Movie>> {
        val movieList = FakeEntityFactory.createFakeMovieList(5) {
            FakeEntityFactory.createFakeMovie().copy(
                genres = listOf(FakeEntityFactory.createGenre(genreId))
            )
        }
        return Result.Success(movieList)
    }
}
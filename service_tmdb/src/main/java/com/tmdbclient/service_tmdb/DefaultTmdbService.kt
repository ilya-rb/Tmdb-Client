package com.tmdbclient.service_tmdb

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.MovieFilter
import com.illiarb.tmdblcient.core.domain.Review
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.service_tmdb.api.DiscoverApi
import com.tmdbclient.service_tmdb.api.GenreApi
import com.tmdbclient.service_tmdb.mappers.GenreListMapper
import com.tmdbclient.service_tmdb.mappers.MovieMapper
import javax.inject.Inject

class DefaultTmdbService @Inject constructor(
    private val repository: MoviesRepository,
    private val genreListMapper: GenreListMapper,
    private val movieMapper: MovieMapper,
    private val genreApi: GenreApi,
    private val discoverApi: DiscoverApi
) : TmdbService {

    override suspend fun getAllMovies(): Result<List<MovieBlock>> = Result.create {
        repository.getMovieFilters().map { MovieBlock(it, getMoviesByType(it)) }
    }

    override suspend fun getMovieGenres(): Result<List<Genre>> =
        Result.create { genreListMapper.map(genreApi.getGenresAsync().await()) }

    override suspend fun getMovieDetails(id: Int): Result<Movie> =
        Result.create { repository.getMovieDetails(id, "images,reviews") }

    override suspend fun getMovieReviews(id: Int): Result<List<Review>> =
        Result.create { repository.getMovieReviews(id) }

    private suspend fun getMoviesByType(filter: MovieFilter): List<Movie> =
        repository.getMoviesByType(filter.code, false)

    override suspend fun discoverMovies(): Result<List<Movie>> = Result.create {
        val results = discoverApi.discoverMoviesAsync().await()
        movieMapper.mapList(results.results)
    }
}
package com.tmdbclient.service_tmdb

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.MovieFilter
import com.illiarb.tmdblcient.core.domain.Review
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.util.Either
import com.tmdbclient.service_tmdb.api.GenreApi
import com.tmdbclient.service_tmdb.mappers.GenreListMapper
import javax.inject.Inject

class DefaultTmdbService @Inject constructor(
    private val repository: MoviesRepository,
    private val genreListMapper: GenreListMapper,
    private val genreApi: GenreApi
) : TmdbService {

    override suspend fun getAllMovies(): Either<List<MovieBlock>> = Either.create {
        repository.getMovieFilters().map { MovieBlock(it, getMoviesByType(it)) }
    }

    override suspend fun getMovieGenres(): Either<List<Genre>> =
        Either.create { genreListMapper.map(genreApi.getGenresAsync().await()) }

    override suspend fun getMovieDetails(id: Int): Either<Movie> =
        Either.create { repository.getMovieDetails(id, "images,reviews") }

    override suspend fun getMovieReviews(id: Int): Either<List<Review>> =
        Either.create { repository.getMovieReviews(id) }

    private suspend fun getMoviesByType(filter: MovieFilter): List<Movie> =
        repository.getMoviesByType(filter.code, true)
}
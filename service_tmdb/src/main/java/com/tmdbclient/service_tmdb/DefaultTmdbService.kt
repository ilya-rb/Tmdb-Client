package com.tmdbclient.service_tmdb

import com.illiarb.tmdblcient.core.domain.*
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import com.illiarb.tmdblcient.core.util.*
import com.tmdbclient.service_tmdb.api.GenreApi
import com.tmdbclient.service_tmdb.mappers.GenreListMapper
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DefaultTmdbService @Inject constructor(
    private val repository: MoviesRepository,
    private val genreListMapper: GenreListMapper,
    private val genreApi: GenreApi,
    private val dispatcherProvider: DispatcherProvider
) : TmdbService {

    override fun getAllMovies(): Flow<Either<List<MovieBlock>>> = Either.asFlow {
        repository.getMovieFilters().map {
            MovieBlock(it, getMoviesByType(it))
        }
    }

    override fun getMovieGenres(): Flow<Either<List<Genre>>> =
        Either.asFlow { genreListMapper.map(genreApi.getGenresAsync().await()) }
            .flowOn(dispatcherProvider.io)

    override fun getMovieDetails(id: Int): Flow<Either<Movie>> =
        Either.asFlow { repository.getMovieDetails(id, "images,reviews") }

    override fun getMovieReviews(id: Int): Flow<Either<List<Review>>> =
        Either.asFlow { repository.getMovieReviews(id) }

    private suspend fun getMoviesByType(filter: MovieFilter): List<Movie> =
        repository.getMoviesByType(filter.code, true)
}
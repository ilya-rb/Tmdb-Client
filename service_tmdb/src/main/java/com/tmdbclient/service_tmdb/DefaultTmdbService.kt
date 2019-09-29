package com.tmdbclient.service_tmdb

import com.illiarb.tmdblcient.core.domain.*
import com.illiarb.tmdblcient.core.util.Async
import com.illiarb.tmdblcient.core.util.Fail
import com.illiarb.tmdblcient.core.util.Loading
import com.illiarb.tmdblcient.core.util.Success
import com.illiarb.tmdblcient.core.services.TmdbService
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import com.tmdbclient.service_tmdb.api.GenreApi
import com.tmdbclient.service_tmdb.mappers.GenreListMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class DefaultTmdbService @Inject constructor(
    private val repository: MoviesRepository,
    private val genreListMapper: GenreListMapper,
    private val genreApi: GenreApi,
    private val dispatcherProvider: DispatcherProvider
) : TmdbService {

    override fun getAllMovies(): Flow<List<MovieBlock>> = flow {
        emit(
            repository.getMovieFilters().map { MovieBlock(it, getMoviesByType(it)) }
        )
    }

    override fun getMovieDetails(id: Int): Flow<Async<Movie>> = flow {
        emit(Loading())

        try {
            emit(Success(repository.getMovieDetails(id, "images,reviews")))
        } catch (e: Exception) {
            emit(Fail(e))
        }
    }

    override fun getMovieReviews(id: Int): Flow<Async<List<Review>>> = flow {
        emit(Loading())

        try {
            emit(Success(repository.getMovieReviews(id)))
        } catch (e: Exception) {
            emit(Fail(e))
        }
    }

    override fun getMovieGenres(): Flow<List<Genre>> =
        flow { emit(genreListMapper.map(genreApi.getGenresAsync().await())) }
            .flowOn(dispatcherProvider.io)

    private suspend fun getMoviesByType(filter: MovieFilter): List<Movie> =
        repository.getMoviesByType(filter.code, true)
}
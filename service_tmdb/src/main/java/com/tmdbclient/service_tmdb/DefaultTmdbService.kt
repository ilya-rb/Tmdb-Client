package com.tmdbclient.service_tmdb

import com.illiarb.tmdblcient.core.util.Async
import com.illiarb.tmdblcient.core.util.Fail
import com.illiarb.tmdblcient.core.util.Loading
import com.illiarb.tmdblcient.core.util.Success
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.MovieFilter
import com.illiarb.tmdblcient.core.domain.Review
import com.illiarb.tmdblcient.core.services.TmdbService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultTmdbService @Inject constructor(
    private val repository: MoviesRepository
) : TmdbService {

    override fun getAllMovies(): Flow<Async<List<MovieBlock>>> = flow {
        emit(Loading())

        try {
            val blocks = repository.getMovieFilters().map {
                MovieBlock(it, getMoviesByType(it))
            }
            emit(Success(blocks))
        } catch (e: Exception) {
            emit(Fail(e))
        }
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

    private suspend fun getMoviesByType(filter: MovieFilter): List<Movie> =
        repository.getMoviesByType(filter.code, true)
}
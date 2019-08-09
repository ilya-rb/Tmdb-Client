package com.illiarb.tmdbclient.home.domain

import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.MoviesInteractor
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.domain.entity.MovieBlock
import com.illiarb.tmdblcient.core.domain.entity.MovieFilter
import com.illiarb.tmdblcient.core.domain.entity.Review
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.illiarb.tmdblcient.core.storage.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author ilya-rb on 18.02.19.
 */
class MoviesInteractorImpl @Inject constructor(
    private val repository: MoviesRepository,
    private val errorHandler: ErrorHandler
) : MoviesInteractor {

    override suspend fun getAllMovies(): Result<List<MovieBlock>> =
        Result.create(errorHandler) {
            val filters = repository.getMovieFilters()

            filters.map {
                MovieBlock(it, getMoviesByType(it))
            }
        }

    override suspend fun getMovieDetails(id: Int): Result<Movie> =
        Result.create(errorHandler) { repository.getMovieDetails(id, "images,reviews") }

    override suspend fun getMovieReviews(id: Int): Result<List<Review>> =
        Result.create(errorHandler) { repository.getMovieReviews(id) }

    private suspend fun getMoviesByType(filter: MovieFilter): List<Movie> =
        repository.getMoviesByType(filter.code, false)
}
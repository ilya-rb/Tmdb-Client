package com.illiarb.tmdbclient.feature.search.domain

import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.NonBlockingUseCase
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.illiarb.tmdblcient.core.storage.MoviesRepository
import com.illiarb.tmdblcient.core.common.NonBlocking
import javax.inject.Inject

/**
 * @author ilya-rb on 10.01.19.
 */
class SearchMovies @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val errorHandler: ErrorHandler
) : NonBlockingUseCase<List<Movie>, String> {

    @NonBlocking
    override suspend fun executeAsync(payload: String): Result<List<Movie>> {
        return Result.create(errorHandler) {
            moviesRepository.searchMovies(payload)
        }
    }
}
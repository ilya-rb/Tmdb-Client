package com.illiarb.tmdbclient.feature.search.domain

import com.illiarb.tmdblcient.core.domain.NonBlockingUseCase
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import com.illiarb.tmdblcient.core.system.coroutine.NonBlocking
import javax.inject.Inject

/**
 * @author ilya-rb on 10.01.19.
 */
class SearchMovies @Inject constructor(
    private val moviesRepository: MoviesRepository
) : NonBlockingUseCase<List<Movie>, String> {

    @NonBlocking
    override suspend fun executeAsync(payload: String): List<Movie> {
        return moviesRepository.searchMovies(payload)
    }
}
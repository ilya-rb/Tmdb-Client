package com.illiarb.tmdbclient.feature.search.domain

import com.illiarb.tmdblcient.core.domain.UseCase
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import com.illiarb.tmdblcient.core.system.NonBlocking
import javax.inject.Inject

/**
 * @author ilya-rb on 10.01.19.
 */
class SearchMovies @Inject constructor(
    private val moviesRepository: MoviesRepository
) : UseCase<List<Movie>, String> {

    @NonBlocking
    override suspend fun execute(payload: String): List<Movie> {
        return moviesRepository.searchMovies(payload)
    }
}
package com.illiarb.tmdbclient.feature.home.domain

import com.illiarb.tmdblcient.core.domain.UseCase
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.repository.MoviesRepository
import com.illiarb.tmdblcient.core.system.NonBlocking
import javax.inject.Inject

/**
 * @author ilya-rb on 09.01.19.
 */
class GetMovieDetails @Inject constructor(
    private val moviesRepository: MoviesRepository
) : UseCase<Movie, Int> {

    @NonBlocking
    override suspend fun execute(payload: Int): Movie {
        return moviesRepository.getMovieDetails(payload, "images,reviews")
    }
}
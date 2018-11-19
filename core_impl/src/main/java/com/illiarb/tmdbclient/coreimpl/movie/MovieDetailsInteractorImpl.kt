package com.illiarb.tmdbclient.coreimpl.movie

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.modules.movie.MovieDetailsInteractor
import com.illiarb.tmdblcient.core.storage.MoviesRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 18.11.18.
 */
class MovieDetailsInteractorImpl @Inject constructor(
    private val moviesRepository: MoviesRepository
) : MovieDetailsInteractor {

    override fun getMovieDetails(id: Int, appendToResponse: String): Single<Movie> =
        moviesRepository.getMovieDetails(id, appendToResponse)

    override fun getMovieReviews(id: Int): Single<List<Review>> =
        moviesRepository.getMovieReviews(id)
}
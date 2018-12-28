package com.illiarb.tmdbclient.feature.home.details.interactor

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.modules.movie.MovieDetailsInteractor
import com.illiarb.tmdblcient.core.modules.movie.MoviesRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author ilya-rb on 24.12.18.
 */
class MovieDetailsInteractorImpl @Inject constructor(
    private val moviesRepository: MoviesRepository
) : MovieDetailsInteractor {

    override fun getMovieDetails(id: Int): Single<Movie> =
        moviesRepository.getMovieDetails(id, "images,reviews,videos")

    override fun getMovieReviews(id: Int): Single<List<Review>> =
        moviesRepository.getMovieReviews(id)

    override fun onGenreSelected(id: Int) {
        // TODO
    }
}
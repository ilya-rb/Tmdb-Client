package com.illiarb.tmdbclient.coreimpl.modules.movie

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import com.illiarb.tmdblcient.core.modules.movie.MoviesRepository
import io.reactivex.Single
import javax.inject.Inject

class MoviesInteractorImpl @Inject constructor(
    private val moviesRepository: MoviesRepository
) : MoviesInteractor {

    override fun getMoviesByType(type: String): Single<List<Movie>> =
        moviesRepository.getMoviesByType(type)

    override fun getMovieDetails(id: Int, appendToResponse: String): Single<Movie> =
        moviesRepository.getMovieDetails(id, appendToResponse)

    override fun getMovieReviews(id: Int): Single<List<Review>> =
        moviesRepository.getMovieReviews(id)

    override fun getMovieFilters(): Single<List<MovieFilter>> =
        moviesRepository.getMovieFilters()
}
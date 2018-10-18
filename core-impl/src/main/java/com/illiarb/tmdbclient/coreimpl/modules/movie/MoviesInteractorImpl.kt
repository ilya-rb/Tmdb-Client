package com.illiarb.tmdbclient.coreimpl.modules.movie

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.modules.movie.MoviesApi
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import io.reactivex.Single
import javax.inject.Inject

class MoviesInteractorImpl @Inject constructor(
    private val moviesApi: MoviesApi
) : MoviesInteractor {

    override fun getMoviesByType(type: String): Single<List<Movie>> =
        moviesApi.getMoviesByType(type)

    override fun getMovieDetails(id: Int, appendToResponse: String): Single<Movie> =
        moviesApi.getMovieDetails(id, appendToResponse)

    override fun getMovieReviews(id: Int): Single<List<Review>> =
        moviesApi.getMovieReviews(id)
}
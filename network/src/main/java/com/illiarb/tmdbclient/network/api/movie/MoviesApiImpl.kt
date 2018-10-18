package com.illiarb.tmdbclient.network.api.movie

import com.illiarb.tmdbclient.network.mappers.MovieMapper
import com.illiarb.tmdbclient.network.mappers.ReviewMapper
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.Review
import com.illiarb.tmdblcient.core.modules.movie.MoviesApi
import io.reactivex.Single
import javax.inject.Inject

class MoviesApiImpl @Inject constructor(
    private val moviesService: MovieService,
    private val movieMapper: MovieMapper,
    private val reviewMapper: ReviewMapper
) : MoviesApi {

    override fun getMoviesByType(type: String): Single<List<Movie>> =
        moviesService.getMoviesByType(type).map { movieMapper.mapList(it.results) }

    override fun getMovieDetails(id: Int, appendToResponse: String): Single<Movie> =
        moviesService.getMovieDetails(id, appendToResponse).map(movieMapper::map)

    override fun getMovieReviews(id: Int): Single<List<Review>> =
        moviesService.getMovieReviews(id).map { reviewMapper.mapList(it.results) }
}
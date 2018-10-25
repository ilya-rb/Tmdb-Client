package com.illiarb.tmdbclient.storage.network.api.movie

import com.illiarb.tmdbclient.storage.dto.MovieDto
import com.illiarb.tmdbclient.storage.dto.ReviewDto
import io.reactivex.Single
import javax.inject.Inject

class MoviesApi @Inject constructor(private val moviesService: MovieService) {

    fun getMoviesByType(type: String): Single<List<MovieDto>> =
        moviesService.getMoviesByType(type).map { it.results }

    fun getMovieDetails(id: Int, appendToResponse: String): Single<MovieDto> =
        moviesService.getMovieDetails(id, appendToResponse)

    fun getMovieReviews(id: Int): Single<List<ReviewDto>> =
        moviesService.getMovieReviews(id).map { it.results }
}
package com.illiarb.tmdbclient.movies.domain

import com.illiarb.tmdblcient.core.domain.MoviesInteractor
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.domain.entity.MovieBlock
import com.illiarb.tmdblcient.core.domain.entity.MovieFilter
import com.illiarb.tmdblcient.core.domain.entity.Review
import com.illiarb.tmdblcient.core.storage.MoviesRepository
import javax.inject.Inject

/**
 * @author ilya-rb on 18.02.19.
 */
class MoviesInteractorImpl @Inject constructor(
    private val repository: MoviesRepository
) : MoviesInteractor {

    override suspend fun getAllMovies(): List<MovieBlock> =
        repository.getMovieFilters().map { MovieBlock(it, getMoviesByType(it)) }

    override suspend fun getMovieDetails(id: Int): Movie = repository.getMovieDetails(id, "images,reviews")

    override suspend fun getMovieReviews(id: Int): List<Review> = repository.getMovieReviews(id)

    private suspend fun getMoviesByType(filter: MovieFilter): List<Movie> =
        repository.getMoviesByType(filter.code, false)
}
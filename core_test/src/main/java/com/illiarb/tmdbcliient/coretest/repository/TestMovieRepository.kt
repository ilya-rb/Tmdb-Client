package com.illiarb.tmdbcliient.coretest.repository

import com.illiarb.tmdbcliient.coretest.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieFilter
import com.illiarb.tmdblcient.core.domain.Review
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.repository.MoviesRepository
import java.util.Collections

@Suppress("MagicNumber")
class TestMovieRepository : MoviesRepository {

    private val movieFilters = listOf(
        MovieFilter("Now playing", MovieFilter.TYPE_NOW_PLAYING),
        MovieFilter("Popular", MovieFilter.TYPE_POPULAR),
        MovieFilter("Upcoming", MovieFilter.TYPE_UPCOMING),
        MovieFilter("Top rated", MovieFilter.TYPE_TOP_RATED)
    )

    override suspend fun getMoviesByType(
        type: String,
        refresh: Boolean
    ): Result<List<Movie>> {
        val size = 10

        return Result.Success(
            mutableListOf<Movie>().apply {
                for (i in 0..size) {
                    add(FakeEntityFactory.createFakeMovie())
                }
            }
        )
    }

    override suspend fun getMovieDetails(id: Int, appendToResponse: String): Result<Movie> {
        var movie = FakeEntityFactory.createFakeMovie()
        if (appendToResponse.contains(MoviesInteractor.KEY_INCLUDE_IMAGES)) {
            movie = movie.copy(images = listOf("image1", "image2"))
        }
        return Result.Success(movie)
    }

    override suspend fun getMovieReviews(id: Int): Result<List<Review>> =
        Result.Success(Collections.emptyList())

    override suspend fun getMovieFilters(): Result<List<MovieFilter>> = Result.Success(movieFilters)
}
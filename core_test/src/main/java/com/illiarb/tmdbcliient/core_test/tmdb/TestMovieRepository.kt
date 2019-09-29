package com.illiarb.tmdbcliient.core_test.tmdb

import com.illiarb.tmdbcliient.core_test.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieFilter
import com.illiarb.tmdblcient.core.domain.Review
import com.tmdbclient.service_tmdb.MoviesRepository
import java.util.Collections

class TestMovieRepository : MoviesRepository {

    private val movieFilters = listOf(
        MovieFilter("Popular", MovieFilter.TYPE_POPULAR),
        MovieFilter("Now playing", MovieFilter.TYPE_NOW_PLAYING),
        MovieFilter("Upcoming", MovieFilter.TYPE_UPCOMING),
        MovieFilter("Top rated", MovieFilter.TYPE_TOP_RATED)
    )

    override suspend fun getMoviesByType(type: String, refresh: Boolean): List<Movie> =
        Collections.emptyList()

    override suspend fun getMovieDetails(id: Int, appendToResponse: String): Movie =
        FakeEntityFactory.createFakeMovie()

    override suspend fun getMovieReviews(id: Int): List<Review> = Collections.emptyList()

    override suspend fun getMovieFilters(): List<MovieFilter> = movieFilters
}
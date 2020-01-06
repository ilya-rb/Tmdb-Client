package com.illiarb.tmdbcliient.coretest.entity

import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieFilter

/**
 * Factory class to create entities for tests
 *
 * @author ilya-rb on 24.01.19.
 */
@Suppress("MagicNumber")
object FakeEntityFactory {

    fun createFakeMovie(init: () -> Movie = defaultMovieCreator()): Movie = init()

    fun createFakeMovieList(
        creator: () -> Movie = defaultMovieCreator(),
        size: Int
    ): List<Movie> = mutableListOf<Movie>().apply {
        for (i in 0..size) {
            add(creator.invoke())
        }
    }

    fun createMovieFilters(): List<MovieFilter> = mutableListOf<MovieFilter>().apply {
        add(MovieFilter(MovieFilter.TYPE_NOW_PLAYING, "now_playing"))
        add(MovieFilter(MovieFilter.TYPE_POPULAR, "popular"))
        add(MovieFilter(MovieFilter.TYPE_UPCOMING, "upcoming"))
        add(MovieFilter(MovieFilter.TYPE_TOP_RATED, "top_rated"))
    }

    private fun defaultMovieCreator(): () -> Movie = {
        Movie(
            123123,
            "/ffgmdfg",
            "/kgdg445",
            emptyList(),
            "http://google.com",
            emptyList(),
            "2018-01-12",
            "overview",
            emptyList(),
            120,
            "Movie title",
            emptyList(),
            7.4f,
            9,
            ""
        )
    }
}
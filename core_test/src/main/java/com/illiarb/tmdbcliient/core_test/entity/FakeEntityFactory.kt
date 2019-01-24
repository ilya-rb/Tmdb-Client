package com.illiarb.tmdbcliient.core_test.entity

import com.illiarb.tmdblcient.core.entity.Movie

/**
 * @author ilya-rb on 24.01.19.
 */
object FakeEntityFactory {

    fun createFakeMovie(): Movie = Movie(
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
        9
    )

    fun createFakeMovieList(size: Int): List<Movie> = mutableListOf<Movie>().apply {
        for (i in 0..size) {
            add(createFakeMovie())
        }
    }
}
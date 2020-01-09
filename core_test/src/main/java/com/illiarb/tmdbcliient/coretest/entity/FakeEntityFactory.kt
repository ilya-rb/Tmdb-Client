package com.illiarb.tmdbcliient.coretest.entity

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie

/**
 * Factory class to create entities for tests
 *
 * @author ilya-rb on 24.01.19.
 */
@Suppress("MagicNumber")
object FakeEntityFactory {

    fun createGenre(id: Int = 1, title: String = "Drama"): Genre = Genre(id, title)

    fun createFakeMovie(init: () -> Movie = defaultMovieCreator()): Movie = init()

    fun createFakeMovieList(
        size: Int,
        creator: () -> Movie = defaultMovieCreator()
    ): List<Movie> = mutableListOf<Movie>().apply {
        for (i in 0..size) {
            add(creator.invoke())
        }
    }

    private fun defaultMovieCreator(): () -> Movie = {
        Movie(
            123123,
            null,
            null,
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
            "",
            9
        )
    }
}
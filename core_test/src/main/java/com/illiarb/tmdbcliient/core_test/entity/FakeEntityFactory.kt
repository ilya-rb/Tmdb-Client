package com.illiarb.tmdbcliient.core_test.entity

import com.illiarb.tmdblcient.core.entity.Account
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.UserCredentials
import java.util.*

/**
 * Factory class to create entities for tests
 *
 * @author ilya-rb on 24.01.19.
 */
object FakeEntityFactory {

    fun createFakeMovie(init: () -> Movie = defaultMovieCreator()): Movie {
        return init()
    }

    fun createFakeAccount(): Account = Account(
        123,
        "Name",
        "Username",
        "/fsdfs3434",
        10,
        Collections.emptyList()
    )

    fun createFakeMovieList(
        creator: () -> Movie = defaultMovieCreator(),
        size: Int
    ): List<Movie> = mutableListOf<Movie>().apply {
        for (i in 0..size) {
            add(creator.invoke())
        }
    }

    fun createValidCredentials() = UserCredentials("username", "password")

    fun createUsernameEmptyCredentials() = UserCredentials("", "password")

    fun createPasswordInvalidCredentials() = UserCredentials("username", "pas")

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
            9
        )
    }
}
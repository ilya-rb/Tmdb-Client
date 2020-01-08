package com.illiarb.tmdbcliient.coretest.repository

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.repository.GenresRepository

class TestGenresRepository : GenresRepository {

    @Suppress("MagicNumber")
    private val testGenres = listOf(
        Genre(0, "Action"),
        Genre(1, "Adventure"),
        Genre(2, "Animation"),
        Genre(3, "Comedy"),
        Genre(4, "Crime"),
        Genre(5, "Documentary"),
        Genre(6, "War"),
        Genre(7, "Thriller"),
        Genre(8, "Horror")
    )

    override suspend fun getGenres(): Result<List<Genre>> {
        return Result.Success(testGenres)
    }
}
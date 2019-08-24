package com.illiarb.tmdblcient.core.domain

import com.illiarb.tmdblcient.core.domain.entity.Movie

interface SearchInteractor {

    suspend fun searchMovies(query: String): List<Movie>
}
package com.illiarb.tmdbclient.movies.domain

import com.illiarb.tmdblcient.core.domain.SearchInteractor
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.storage.MoviesRepository
import javax.inject.Inject

class SearchInteractorImpl @Inject constructor(
    private val moviesRepository: MoviesRepository
) : SearchInteractor {

    override suspend fun searchMovies(query: String): List<Movie> {
        return moviesRepository.searchMovies(query)
    }
}
package com.illiarb.tmdbclient.feature.search.domain

import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.SearchInteractor
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.storage.ErrorHandler
import com.illiarb.tmdblcient.core.storage.MoviesRepository
import javax.inject.Inject

/**
 * @author ilya-rb on 18.02.19.
 */
class SearchInteractorImpl @Inject constructor(
    private val repository: MoviesRepository,
    private val errorHandler: ErrorHandler,
    private val analyticsService: AnalyticsService
) : SearchInteractor {

    override suspend fun searchMovies(query: String): Result<List<Movie>> =
        Result.create(errorHandler) {
            repository.searchMovies(query).also {
                analyticsService.trackEvent(analyticsService.factory.createMovieSearchEvent(query))
            }
        }
}
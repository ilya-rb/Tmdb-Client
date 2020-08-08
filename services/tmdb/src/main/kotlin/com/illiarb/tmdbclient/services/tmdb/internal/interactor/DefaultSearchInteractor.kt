package com.illiarb.tmdbclient.services.tmdb.internal.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.PagedList
import com.illiarb.tmdbclient.services.tmdb.interactor.SearchInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.MovieMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.MovieApi
import com.illiarb.tmdbclient.services.tmdb.internal.repository.ConfigurationRepository
import javax.inject.Inject

internal class DefaultSearchInteractor @Inject constructor(
  private val configurationRepository: ConfigurationRepository,
  private val movieApi: MovieApi,
  private val movieMapper: MovieMapper
) : SearchInteractor {

  override suspend fun searchMovies(query: String): Result<PagedList<Movie>> {
    val config = configurationRepository.getConfiguration()
    if (config.isError()) {
      return Result.Err(config.error())
    }

    val searchResult = movieApi.searchMovies(query)

    return searchResult.mapOnSuccess {
      PagedList(movieMapper.mapList(config.unwrap(), it.results), it.page, it.totalPages)
    }
  }
}
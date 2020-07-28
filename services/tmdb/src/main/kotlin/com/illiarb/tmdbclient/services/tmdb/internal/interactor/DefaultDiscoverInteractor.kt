package com.illiarb.tmdbclient.services.tmdb.internal.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Filter
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.PagedList
import com.illiarb.tmdbclient.services.tmdb.domain.YearConstraints
import com.illiarb.tmdbclient.services.tmdb.interactor.DiscoverInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.MovieMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.DiscoverApi
import com.illiarb.tmdbclient.services.tmdb.internal.repository.ConfigurationRepository
import com.illiarb.tmdbclient.services.tmdb.internal.util.TmdbDateFormatter
import javax.inject.Inject

internal class DefaultDiscoverInteractor @Inject constructor(
  private val configurationRepository: ConfigurationRepository,
  private val dateFormatter: TmdbDateFormatter,
  private val discoverApi: DiscoverApi,
  private val movieMapper: MovieMapper
) : DiscoverInteractor {

  companion object {
    const val PRIMARY_RELEASE_DATE_GREATER_THAN = "primary_release_date.gte"
    const val PRIMARY_RELEASE_DATE_LOWER_THAN = "primary_release_date.gte"
    const val YEAR = "year"
    const val GENRES = "with_genres"
  }

  override suspend fun discoverMovies(filter: Filter, page: Int): Result<PagedList<Movie>> {
    val config = configurationRepository.getConfiguration()
    if (config.isError()) {
      return Result.Err(config.error())
    }

    val filtersMap = mutableMapOf<String, String>().apply {
      val ids = filter.selectedGenreIds.joinToString(",")
      if (ids.isNotEmpty()) {
        put(GENRES, ids)
      }

      when (val years = filter.yearConstraints) {
        is YearConstraints.SingleYear -> put(YEAR, years.year.toString())
        is YearConstraints.YearRange -> {
          put(PRIMARY_RELEASE_DATE_GREATER_THAN, dateFormatter.yearToReleaseDate(years.startYear))
          put(PRIMARY_RELEASE_DATE_LOWER_THAN, dateFormatter.yearToReleaseDate(years.endYear))
        }
      }
    }

    return discoverApi.discoverMovies(
      filters = filtersMap,
      page = page
    ).mapOnSuccess {
      PagedList(movieMapper.mapList(config.unwrap(), it.results), it.page, it.totalPages)
    }
  }
}
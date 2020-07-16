package com.illiarb.tmdbclient.services.tmdb.internal.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.TrendingSection
import com.illiarb.tmdbclient.services.tmdb.interactor.TrendingInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.TrendingMapper
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.TrendingApi
import com.illiarb.tmdbclient.services.tmdb.internal.repository.ConfigurationRepository
import javax.inject.Inject

internal class DefaultTrendingInteractor @Inject constructor(
  private val api: TrendingApi,
  private val mapper: TrendingMapper,
  private val configurationRepository: ConfigurationRepository
) : TrendingInteractor {

  override suspend fun getTrending(): Result<List<TrendingSection.TrendingItem>> {
    val configuration = configurationRepository.getConfiguration()
    if (configuration.isError()) {
      return Result.Err(configuration.error())
    }

    return api.getTrendingAsync(
      TrendingApi.TRENDING_TYPE_MOVIES,
      TrendingApi.TRENDING_THIS_WEEK
    ).mapOnSuccess {
      mapper.mapList(configuration.unwrap(), it.results)
    }
  }
}
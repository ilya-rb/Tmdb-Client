package com.illiarb.tmdbclient.services.tmdb.internal.interactor

import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.services.tmdb.domain.TrendingSection
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.interactor.TrendingInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.network.api.TrendingApi
import com.illiarb.tmdbclient.services.tmdb.internal.cache.TmdbCache
import com.illiarb.tmdbclient.services.tmdb.internal.network.mappers.TrendingMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultTrendingInteractor @Inject constructor(
  private val api: TrendingApi,
  private val mapper: TrendingMapper,
  private val cache: TmdbCache,
  private val dispatcherProvider: DispatcherProvider
) : TrendingInteractor {

  override suspend fun getTrending(): Result<List<TrendingSection.TrendingItem>> {
    val configuration = withContext(dispatcherProvider.io) { cache.getConfiguration() }
    return api.getTrendingAsync(
      TrendingApi.TRENDING_TYPE_MOVIES,
      TrendingApi.TRENDING_THIS_WEEK
    ).mapOnSuccess {
      mapper.mapList(configuration, it.results)
    }
  }
}
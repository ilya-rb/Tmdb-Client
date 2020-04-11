package com.tmdbclient.servicetmdb.internal.interactor

import com.illiarb.tmdbclient.tools.DispatcherProvider
import com.tmdbclient.servicetmdb.domain.TrendingSection
import com.illiarb.tmdbclient.util.Result
import com.tmdbclient.servicetmdb.interactor.TrendingInteractor
import com.tmdbclient.servicetmdb.internal.network.api.TrendingApi
import com.tmdbclient.servicetmdb.internal.cache.TmdbCache
import com.tmdbclient.servicetmdb.internal.network.mappers.TrendingMapper
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
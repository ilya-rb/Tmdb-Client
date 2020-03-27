package com.tmdbclient.servicetmdb.interactor

import com.illiarb.tmdblcient.core.interactor.TrendingInteractor
import com.illiarb.tmdblcient.core.domain.TrendingSection
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.api.TrendingApi
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.mappers.TrendingMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultTrendingInteractor @Inject constructor(
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
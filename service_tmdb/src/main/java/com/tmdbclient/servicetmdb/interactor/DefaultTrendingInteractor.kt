package com.tmdbclient.servicetmdb.interactor

import com.illiarb.tmdblcient.core.interactor.TrendingInteractor
import com.illiarb.tmdblcient.core.domain.TrendingSection
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.api.TrendingApi
import com.tmdbclient.servicetmdb.mappers.TrendingMapper
import javax.inject.Inject

class DefaultTrendingInteractor @Inject constructor(
  private val api: TrendingApi,
  private val mapper: TrendingMapper
) : TrendingInteractor {

  override suspend fun getTrending(): Result<List<TrendingSection.TrendingItem>> {
    return Result.create {
      val results = api.getTrendingAsync(
        TrendingApi.TRENDING_TYPE_MOVIES,
        TrendingApi.TRENDING_THIS_WEEK
      ).await()

      mapper.mapList(results.results)
    }
  }
}
package com.illiarb.tmdbcliient.coretest.interactor

import com.illiarb.tmdbclient.util.Result
import com.illiarb.tmdbcliient.coretest.entity.FakeEntityFactory
import com.tmdbclient.servicetmdb.domain.TrendingSection
import com.tmdbclient.servicetmdb.interactor.TrendingInteractor

class TestTrendingInteractor : TrendingInteractor {

  override suspend fun getTrending(): Result<List<TrendingSection.TrendingItem>> {
    return Result.Ok(
      listOf(
        TrendingSection.TrendingItem(
          FakeEntityFactory.createFakeMovie()
        )
      )
    )
  }
}
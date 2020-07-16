package com.illiarb.tmdbclient.libs.test.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.libs.test.entity.FakeEntityFactory
import com.illiarb.tmdbclient.services.tmdb.domain.TrendingSection
import com.illiarb.tmdbclient.services.tmdb.interactor.TrendingInteractor

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
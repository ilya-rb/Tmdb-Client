package com.illiarb.tmdbcliient.coretest.interactor

import com.illiarb.tmdblcient.core.domain.TrendingSection
import com.illiarb.tmdblcient.core.interactor.TrendingInteractor
import com.illiarb.tmdblcient.core.util.Result

class TestTrendingInteractor : TrendingInteractor {

    override suspend fun getTrending(): Result<List<TrendingSection.TrendingItem>> {
        return Result.Success(listOf(
            TrendingSection.TrendingItem(
                "image",
                "name"
            )
        ))
    }
}
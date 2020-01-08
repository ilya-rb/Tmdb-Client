package com.illiarb.tmdblcient.core.interactor

import com.illiarb.tmdblcient.core.domain.TrendingSection.TrendingItem
import com.illiarb.tmdblcient.core.util.Result

interface TrendingInteractor {

    suspend fun getTrending(): Result<List<TrendingItem>>
}
package com.tmdbclient.servicetmdb.interactor

import com.tmdbclient.servicetmdb.domain.TrendingSection.TrendingItem
import com.illiarb.tmdbclient.util.Result

interface TrendingInteractor {

  suspend fun getTrending(): Result<List<TrendingItem>>
}
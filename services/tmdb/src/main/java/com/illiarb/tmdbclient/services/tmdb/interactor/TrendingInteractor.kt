package com.illiarb.tmdbclient.services.tmdb.interactor

import com.illiarb.tmdbclient.services.tmdb.domain.TrendingSection.TrendingItem
import com.illiarb.tmdbclient.libs.util.Result

interface TrendingInteractor {

  suspend fun getTrending(): Result<List<TrendingItem>>
}
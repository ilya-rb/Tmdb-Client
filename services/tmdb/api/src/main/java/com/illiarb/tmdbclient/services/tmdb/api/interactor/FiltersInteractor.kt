package com.illiarb.tmdbclient.services.tmdb.api.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.api.domain.Filter
import kotlinx.coroutines.flow.Flow

interface FiltersInteractor {

  val filter: Flow<Filter>

  suspend fun saveFilter(filter: Filter): Result<Unit>

  suspend fun getFilter(): Result<Filter>
}
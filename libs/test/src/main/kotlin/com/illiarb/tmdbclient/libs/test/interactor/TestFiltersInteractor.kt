package com.illiarb.tmdbclient.libs.test.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.api.domain.Filter
import com.illiarb.tmdbclient.services.tmdb.api.interactor.FiltersInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestFiltersInteractor : FiltersInteractor {

  override val filter: Flow<Filter>
    get() = flow { }

  override suspend fun saveFilter(filter: Filter): Result<Unit> {
    return Result.create { }
  }

  override suspend fun getFilter(): Result<Filter> {
    return Result.Ok(Filter.create())
  }
}
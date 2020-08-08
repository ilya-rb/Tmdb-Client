package com.illiarb.tmdbclient.services.tmdb.internal.interactor

import androidx.room.withTransaction
import com.illiarb.tmdbclient.libs.tools.DispatcherProvider
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.services.tmdb.domain.Filter
import com.illiarb.tmdbclient.services.tmdb.interactor.FiltersInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.db.TmdbDatabase
import com.illiarb.tmdbclient.services.tmdb.internal.db.dto.FilterDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultFiltersInteractor @Inject constructor(
  private val db: TmdbDatabase,
  private val dispatcherProvider: DispatcherProvider
) : FiltersInteractor {

  private val filtersDao = db.filterDao()

  override val filter: Flow<Filter>
    get() = filtersDao.filter().map { it?.filter ?: Filter.create() }

  override suspend fun saveFilter(filter: Filter): Result<Unit> {
    return Result.create {
      withContext(dispatcherProvider.io) {
        db.withTransaction {
          filtersDao.storeFilter(FilterDto(filter = filter))
        }
      }
    }
  }

  override suspend fun getFilter(): Result<Filter> {
    return Result.create {
      withContext(dispatcherProvider.io) {
        filtersDao.getFilter()?.filter ?: Filter.create()
      }
    }
  }
}
package com.tmdbclient.servicetmdb.mappers

import com.illiarb.tmdblcient.core.domain.TrendingSection.TrendingItem
import com.illiarb.tmdblcient.core.util.SuspendableMapper
import com.tmdbclient.servicetmdb.model.MovieModel
import com.tmdbclient.servicetmdb.model.PersonModel
import com.tmdbclient.servicetmdb.model.TrendingModel
import com.tmdbclient.servicetmdb.model.TvShowModel
import java.util.Collections
import javax.inject.Inject

class TrendingMapper @Inject constructor(
  private val movieMapper: MovieMapper
) : SuspendableMapper<TrendingModel, TrendingItem> {

  override suspend fun map(from: TrendingModel): TrendingItem {
    return when (from) {
      is MovieModel -> TrendingItem(movieMapper.map(from))
      else -> throw IllegalArgumentException("Unknown trending type")
    }
  }

  override suspend fun mapList(collection: List<TrendingModel>?): List<TrendingItem> {
    if (collection == null) return Collections.emptyList()
    val supported = collection.filter {
      isTypeSupported(it)
    }
    return super.mapList(supported)
  }

  private fun isTypeSupported(item: TrendingModel): Boolean =
    item is MovieModel || item is PersonModel || item is TvShowModel
}
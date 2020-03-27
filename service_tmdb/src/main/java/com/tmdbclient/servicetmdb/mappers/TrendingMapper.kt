package com.tmdbclient.servicetmdb.mappers

import com.illiarb.tmdblcient.core.domain.TrendingSection.TrendingItem
import com.tmdbclient.servicetmdb.configuration.Configuration
import com.tmdbclient.servicetmdb.model.MovieModel
import com.tmdbclient.servicetmdb.model.PersonModel
import com.tmdbclient.servicetmdb.model.TrendingModel
import com.tmdbclient.servicetmdb.model.TvShowModel
import java.util.Collections
import javax.inject.Inject

class TrendingMapper @Inject constructor(private val movieMapper: MovieMapper) {

  fun map(configuration: Configuration, from: TrendingModel): TrendingItem {
    return when (from) {
      is MovieModel -> TrendingItem(movieMapper.map(configuration, from))
      else -> throw IllegalArgumentException("Unknown trending type")
    }
  }

  fun mapList(configuration: Configuration, collection: List<TrendingModel>?): List<TrendingItem> {
    if (collection == null) return Collections.emptyList()
    val supported = collection.filter {
      isTypeSupported(it)
    }
    return supported.map {
      map(configuration, it)
    }
  }

  private fun isTypeSupported(item: TrendingModel): Boolean =
    item is MovieModel || item is PersonModel || item is TvShowModel
}
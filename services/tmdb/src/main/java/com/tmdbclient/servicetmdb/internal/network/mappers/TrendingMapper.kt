package com.tmdbclient.servicetmdb.internal.network.mappers

import com.tmdbclient.servicetmdb.domain.TrendingSection.TrendingItem
import com.tmdbclient.servicetmdb.internal.configuration.Configuration
import com.tmdbclient.servicetmdb.internal.network.model.MovieModel
import com.tmdbclient.servicetmdb.internal.network.model.PersonModel
import com.tmdbclient.servicetmdb.internal.network.model.TrendingModel
import com.tmdbclient.servicetmdb.internal.network.model.TvShowModel
import java.util.Collections
import javax.inject.Inject

internal class TrendingMapper @Inject constructor(private val movieMapper: MovieMapper) {

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
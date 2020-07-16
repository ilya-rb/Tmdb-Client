package com.illiarb.tmdbclient.services.tmdb.internal.mappers

import com.illiarb.tmdbclient.services.tmdb.domain.TrendingSection.TrendingItem
import com.illiarb.tmdbclient.services.tmdb.internal.model.Configuration
import com.illiarb.tmdbclient.services.tmdb.internal.model.MovieModel
import com.illiarb.tmdbclient.services.tmdb.internal.model.PersonModel
import com.illiarb.tmdbclient.services.tmdb.internal.model.TrendingModel
import com.illiarb.tmdbclient.services.tmdb.internal.model.TvShowModel
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
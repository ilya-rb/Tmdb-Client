package com.illiarb.tmdbclient.services.tmdb.internal.mappers

import com.illiarb.tmdbclient.services.tmdb.domain.TrendingSection.TrendingItem
import com.illiarb.tmdbclient.services.tmdb.internal.dto.ConfigurationDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.MovieDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.TrendingDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.TvShowDto
import java.util.Collections
import javax.inject.Inject

internal class TrendingMapper @Inject constructor(private val movieMapper: MovieMapper) {

  fun map(configuration: ConfigurationDto, from: TrendingDto): TrendingItem {
    return when (from) {
      is MovieDto -> TrendingItem(movieMapper.map(configuration, from))
      else -> throw IllegalArgumentException("Unknown trending type")
    }
  }

  fun mapList(configuration: ConfigurationDto, collection: List<TrendingDto>?): List<TrendingItem> {
    if (collection == null) return Collections.emptyList()
    val supported = collection.filter {
      isTypeSupported(it)
    }
    return supported.map {
      map(configuration, it)
    }
  }

  private fun isTypeSupported(item: TrendingDto): Boolean = item is MovieDto || item is TvShowDto
}
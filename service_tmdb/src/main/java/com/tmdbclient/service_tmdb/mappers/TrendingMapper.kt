package com.tmdbclient.service_tmdb.mappers

import com.illiarb.tmdblcient.core.domain.TrendingSection.TrendingItem
import com.illiarb.tmdblcient.core.util.Mapper
import com.tmdbclient.service_tmdb.configuration.ImageType
import com.tmdbclient.service_tmdb.configuration.ImageUrlCreator
import com.tmdbclient.service_tmdb.model.MovieModel
import com.tmdbclient.service_tmdb.model.PersonModel
import com.tmdbclient.service_tmdb.model.TrendingModel
import com.tmdbclient.service_tmdb.model.TvShowModel
import java.util.Collections
import javax.inject.Inject

class TrendingMapper @Inject constructor(
    private val imageUrlCreator: ImageUrlCreator
) : Mapper<TrendingModel, TrendingItem> {

    override fun map(from: TrendingModel): TrendingItem {
        return when (from) {
            is MovieModel -> TrendingItem(
                imageUrlCreator.createImageUrl(from.posterPath, ImageType.Poster),
                from.title
            )

            is TvShowModel -> TrendingItem(
                imageUrlCreator.createImageUrl(from.posterPath, ImageType.Poster),
                from.name
            )

            is PersonModel -> TrendingItem(
                imageUrlCreator.createImageUrl(from.profilePath, ImageType.Profile),
                from.name
            )

            else -> throw IllegalArgumentException("Unknown trending type")
        }
    }

    override fun mapList(collection: List<TrendingModel>?): List<TrendingItem> {
        if (collection == null) return Collections.emptyList()
        val supported = collection.filter { isTypeSupported(it) }
        return super.mapList(supported)
    }

    private fun isTypeSupported(item: TrendingModel): Boolean =
        item is MovieModel || item is PersonModel || item is TvShowModel
}
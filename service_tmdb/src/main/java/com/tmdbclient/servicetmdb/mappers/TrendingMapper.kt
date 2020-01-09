package com.tmdbclient.servicetmdb.mappers

import com.illiarb.tmdblcient.core.domain.TrendingSection.TrendingItem
import com.illiarb.tmdblcient.core.util.SuspendableMapper
import com.tmdbclient.servicetmdb.configuration.ImageType
import com.tmdbclient.servicetmdb.configuration.ImageUrlCreator
import com.tmdbclient.servicetmdb.model.MovieModel
import com.tmdbclient.servicetmdb.model.PersonModel
import com.tmdbclient.servicetmdb.model.TrendingModel
import com.tmdbclient.servicetmdb.model.TvShowModel
import com.tmdbclient.servicetmdb.repository.ConfigurationRepository
import java.util.Collections
import javax.inject.Inject

class TrendingMapper @Inject constructor(
    private val imageUrlCreator: ImageUrlCreator,
    private val configurationRepository: ConfigurationRepository
) : SuspendableMapper<TrendingModel, TrendingItem> {

    override suspend fun map(from: TrendingModel): TrendingItem {
        val result = configurationRepository.getConfiguration().getOrThrow()
        val config = result.images

        return when (from) {
            is MovieModel -> TrendingItem(
                imageUrlCreator.createImage(config, from.posterPath, ImageType.Poster),
                from.title
            )
            is TvShowModel -> TrendingItem(
                imageUrlCreator.createImage(config, from.posterPath, ImageType.Poster),
                from.name
            )
            is PersonModel -> TrendingItem(
                imageUrlCreator.createImage(config, from.profilePath, ImageType.Profile),
                from.name
            )
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
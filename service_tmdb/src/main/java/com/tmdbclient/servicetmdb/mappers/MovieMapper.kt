package com.tmdbclient.servicetmdb.mappers

import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.util.SuspendableMapper
import com.tmdbclient.servicetmdb.configuration.ImageType
import com.tmdbclient.servicetmdb.configuration.ImageUrlCreator
import com.tmdbclient.servicetmdb.model.MovieModel
import com.tmdbclient.servicetmdb.repository.ConfigurationRepository
import javax.inject.Inject

class MovieMapper @Inject constructor(
    private val genreMapper: GenreMapper,
    private val personMapper: PersonMapper,
    private val reviewMapper: ReviewMapper,
    private val configurationRepository: ConfigurationRepository,
    private val imageUrlCreator: ImageUrlCreator
) : SuspendableMapper<MovieModel, Movie> {

    override suspend fun map(from: MovieModel): Movie {
        val result = configurationRepository.getConfiguration().getOrThrow()
        val config = result.images

        return Movie(
            from.id,
            imageUrlCreator.createImage(config, from.posterPath, ImageType.Poster),
            imageUrlCreator.createImage(config, from.backdropPath, ImageType.Backdrop),
            genreMapper.mapList(from.genres),
            from.homepage,
            personMapper.mapList(from.credits?.cast),
            from.releaseDate,
            from.overview,
            reviewMapper.mapList(from.reviews?.results),
            from.runtime,
            from.title,
            from.images?.backdrops?.map {
                imageUrlCreator.createImage(config, it.filePath, ImageType.Backdrop)
            } ?: emptyList(),
            from.voteAverage,
            from.productionCountries.firstOrNull()?.name,
            0,
            from.video
        )
    }
}
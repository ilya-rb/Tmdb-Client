package com.illiarb.tmdbclient.services.tmdb.internal.mappers

import com.illiarb.tmdbclient.libs.util.DisplayFormattedDate
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.internal.model.Configuration
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageType
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageUrlCreator
import com.illiarb.tmdbclient.services.tmdb.internal.model.MovieModel
import com.illiarb.tmdbclient.services.tmdb.internal.util.TmdbDateFormatter
import javax.inject.Inject

internal class MovieMapper @Inject constructor(
  private val genreMapper: GenreMapper,
  private val personMapper: PersonMapper,
  private val reviewMapper: ReviewMapper,
  private val imageUrlCreator: ImageUrlCreator,
  private val tmdbDateFormatter: TmdbDateFormatter
) {

  fun mapList(configuration: Configuration, from: List<MovieModel>): List<Movie> =
    from.map { map(configuration, it) }

  fun map(configuration: Configuration, from: MovieModel): Movie {
    val config = configuration.images

    return Movie(
      from.id,
      imageUrlCreator.createImage(config, from.posterPath, ImageType.Poster),
      imageUrlCreator.createImage(config, from.backdropPath, ImageType.Backdrop),
      genreMapper.mapList(from.genres),
      from.homepage,
      personMapper.mapList(from.credits?.cast),
      DisplayFormattedDate(tmdbDateFormatter.formatDate(from.releaseDate)),
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
      from.videos?.results
    )
  }
}
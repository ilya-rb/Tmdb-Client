package com.illiarb.tmdbclient.services.tmdb.internal.mappers

import com.illiarb.tmdbclient.libs.util.DisplayFormattedDate
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.internal.dto.ConfigurationDto
import com.illiarb.tmdbclient.services.tmdb.internal.dto.MovieDto
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageType
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageUrlCreator
import com.illiarb.tmdbclient.services.tmdb.internal.util.TmdbDateFormatter
import javax.inject.Inject

internal class MovieMapper @Inject constructor(
  private val genreMapper: GenreMapper,
  private val imageUrlCreator: ImageUrlCreator,
  private val tmdbDateFormatter: TmdbDateFormatter
) {

  fun mapList(configuration: ConfigurationDto, from: List<MovieDto>): List<Movie> =
    from.map { map(configuration, it) }

  fun map(configuration: ConfigurationDto, from: MovieDto): Movie {
    val config = configuration.images

    return Movie(
      id = from.id,
      posterPath = imageUrlCreator.createImage(config, from.posterPath, ImageType.Poster),
      backdropPath = imageUrlCreator.createImage(config, from.backdropPath, ImageType.Backdrop),
      genres = genreMapper.mapList(from.genres),
      homepage = from.homepage,
      releaseDate = DisplayFormattedDate(tmdbDateFormatter.formatDate(from.releaseDate)),
      overview = from.overview,
      runtime = from.runtime,
      title = from.title,
      images = from.images?.backdrops?.map {
        imageUrlCreator.createImage(config, it.filePath, ImageType.Backdrop)
      } ?: emptyList(),
      voteAverage = from.voteAverage,
      country = from.productionCountries.firstOrNull()?.name,
      rating = 0,
      videos = from.videos?.results
    )
  }
}
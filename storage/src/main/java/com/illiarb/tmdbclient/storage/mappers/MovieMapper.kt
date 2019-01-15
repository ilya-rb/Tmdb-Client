package com.illiarb.tmdbclient.storage.mappers

import com.illiarb.tmdbclient.storage.model.MovieModel
import com.illiarb.tmdblcient.core.entity.Movie
import java.util.*
import javax.inject.Inject

class MovieMapper @Inject constructor(
    private val genreMapper: GenreMapper,
    private val personMapper: PersonMapper,
    private val reviewMapper: ReviewMapper
) : Mapper<MovieModel, Movie> {

    override fun map(from: MovieModel): Movie =
        Movie(
            from.id,
            from.posterPath,
            from.backdropPath,
            genreMapper.mapList(from.genres),
            from.homepage,
            personMapper.mapList(from.credits?.cast),
            from.releaseDate,
            from.overview,
            reviewMapper.mapList(from.reviews?.results),
            from.runtime,
            from.title,
            from.images?.backdrops?.map { it.filePath } ?: Collections.emptyList(),
            from.voteAverage
        )
}
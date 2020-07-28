package com.illiarb.tmdbclient.services.tmdb.internal.mappers

import com.illiarb.tmdbclient.libs.util.Mapper
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import com.illiarb.tmdbclient.services.tmdb.internal.dto.GenreDto
import javax.inject.Inject

internal class GenreMapper @Inject constructor() : Mapper<GenreDto, Genre> {

  override fun map(from: GenreDto): Genre = Genre(from.id, from.name)
}
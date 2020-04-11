package com.illiarb.tmdbclient.services.tmdb.internal.network.mappers

import com.illiarb.tmdbclient.libs.util.Mapper
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import com.illiarb.tmdbclient.services.tmdb.internal.network.model.GenreModel
import javax.inject.Inject

internal class GenreMapper @Inject constructor() : Mapper<GenreModel, Genre> {

  override fun map(from: GenreModel): Genre = Genre(from.id, from.name)
}
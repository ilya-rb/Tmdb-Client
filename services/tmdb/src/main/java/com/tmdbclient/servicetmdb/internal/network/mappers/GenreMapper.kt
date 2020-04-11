package com.tmdbclient.servicetmdb.internal.network.mappers

import com.illiarb.tmdbclient.util.Mapper
import com.tmdbclient.servicetmdb.domain.Genre
import com.tmdbclient.servicetmdb.internal.network.model.GenreModel
import javax.inject.Inject

internal class GenreMapper @Inject constructor() : Mapper<GenreModel, Genre> {

  override fun map(from: GenreModel): Genre = Genre(from.id, from.name)
}
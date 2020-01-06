package com.tmdbclient.servicetmdb.mappers

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.util.Mapper
import com.tmdbclient.servicetmdb.model.GenreModel
import javax.inject.Inject

class GenreMapper @Inject constructor() : Mapper<GenreModel, Genre> {

    override fun map(from: GenreModel): Genre = Genre(from.id, from.name)
}
package com.illiarb.tmdbclient.storage.mappers

import com.illiarb.tmdbclient.storage.model.GenreModel
import com.illiarb.tmdblcient.core.domain.entity.Genre
import javax.inject.Inject

class GenreMapper @Inject constructor() : Mapper<GenreModel, Genre> {
    override fun map(from: GenreModel): Genre =
        Genre(from.id, from.name)
}
package com.illiarb.tmdbclient.storage.mappers

import com.illiarb.tmdbclient.storage.dto.GenreDto
import com.illiarb.tmdblcient.core.entity.Genre
import javax.inject.Inject

class GenreMapper @Inject constructor() : Mapper<GenreDto, Genre> {

    override fun map(from: GenreDto): Genre = Genre(from.id, from.name)
}
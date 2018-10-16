package com.illiarb.tmdbclient.network.mappers

import com.illiarb.tmdbclient.network.responses.GenreResponse
import com.illiarb.tmdblcient.core.entity.Genre
import javax.inject.Inject

class GenreMapper @Inject constructor() : Mapper<GenreResponse, Genre> {

    override fun map(from: GenreResponse): Genre = Genre(from.id, from.name)
}
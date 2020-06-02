package com.illiarb.tmdbclient.services.tmdb.internal.mappers

import com.illiarb.tmdbclient.libs.util.Mapper
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import com.illiarb.tmdbclient.services.tmdb.internal.model.GenreListModel
import javax.inject.Inject

internal class GenreListMapper @Inject constructor(
  private val genreMapper: GenreMapper
) : Mapper<GenreListModel, List<Genre>> {

  override fun map(from: GenreListModel): List<Genre> = genreMapper.mapList(from.genres)
}
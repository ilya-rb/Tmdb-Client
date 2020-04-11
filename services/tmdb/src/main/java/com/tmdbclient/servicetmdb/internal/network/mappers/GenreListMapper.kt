package com.tmdbclient.servicetmdb.internal.network.mappers

import com.illiarb.tmdbclient.util.Mapper
import com.tmdbclient.servicetmdb.domain.Genre
import com.tmdbclient.servicetmdb.internal.network.model.GenreListModel
import javax.inject.Inject

internal class GenreListMapper @Inject constructor(
  private val genreMapper: GenreMapper
) : Mapper<GenreListModel, List<Genre>> {

  override fun map(from: GenreListModel): List<Genre> = genreMapper.mapList(from.genres)
}
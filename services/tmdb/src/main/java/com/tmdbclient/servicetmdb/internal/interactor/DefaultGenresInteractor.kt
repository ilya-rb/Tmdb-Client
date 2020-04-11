package com.tmdbclient.servicetmdb.internal.interactor

import com.tmdbclient.servicetmdb.domain.Genre
import com.illiarb.tmdbclient.util.Result
import com.tmdbclient.servicetmdb.interactor.GenresInteractor
import com.tmdbclient.servicetmdb.internal.repository.GenresRepository
import javax.inject.Inject

internal class DefaultGenresInteractor @Inject constructor(
  private val genresRepository: GenresRepository
) : GenresInteractor {

  override suspend fun getAllGenres(): Result<List<Genre>> = genresRepository.getGenres()
}
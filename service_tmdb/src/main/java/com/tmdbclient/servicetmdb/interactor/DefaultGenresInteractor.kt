package com.tmdbclient.servicetmdb.interactor

import com.illiarb.tmdblcient.core.interactor.GenresInteractor
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.repository.GenresRepository
import javax.inject.Inject

class DefaultGenresInteractor @Inject constructor(
  private val genresRepository: GenresRepository
) : GenresInteractor {

  override suspend fun getAllGenres(): Result<List<Genre>> = genresRepository.getGenres()
}
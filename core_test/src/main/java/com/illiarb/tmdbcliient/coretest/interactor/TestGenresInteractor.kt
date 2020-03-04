package com.illiarb.tmdbcliient.coretest.interactor

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.interactor.GenresInteractor
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.repository.GenresRepository

class TestGenresInteractor(private val genresRepository: GenresRepository) : GenresInteractor {

  override suspend fun getAllGenres(): Result<List<Genre>> {
    return genresRepository.getGenres()
  }
}
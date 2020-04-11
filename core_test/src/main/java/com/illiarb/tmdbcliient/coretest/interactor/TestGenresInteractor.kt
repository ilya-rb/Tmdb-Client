package com.illiarb.tmdbcliient.coretest.interactor

import com.illiarb.tmdbclient.util.Result
import com.illiarb.tmdbcliient.coretest.entity.FakeEntityFactory
import com.tmdbclient.servicetmdb.domain.Genre
import com.tmdbclient.servicetmdb.interactor.GenresInteractor

class TestGenresInteractor : GenresInteractor {

  override suspend fun getAllGenres(): Result<List<Genre>> = FakeEntityFactory.getGenres()
}
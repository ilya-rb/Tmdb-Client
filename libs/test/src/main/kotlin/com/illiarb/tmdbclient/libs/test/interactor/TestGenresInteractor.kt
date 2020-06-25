package com.illiarb.tmdbclient.libs.test.interactor

import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.libs.test.entity.FakeEntityFactory
import com.illiarb.tmdbclient.services.tmdb.api.domain.Genre
import com.illiarb.tmdbclient.services.tmdb.api.interactor.GenresInteractor

class TestGenresInteractor : GenresInteractor {

  override suspend fun getAllGenres(): Result<List<Genre>> = FakeEntityFactory.getGenres()
}
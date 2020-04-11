package com.illiarb.tmdbcliient.coretest.interactor

import com.illiarb.tmdbclient.util.Result
import com.tmdbclient.servicetmdb.domain.MovieSection
import com.tmdbclient.servicetmdb.interactor.HomeInteractor
import java.util.Collections

class TestHomeInteractor : HomeInteractor {

  override suspend fun getHomeSections(): Result<List<MovieSection>> {
    return Result.Ok(Collections.emptyList())
  }
}